package org.components;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import org.componentannotations.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ComponentManager {

    List<Component> _components = new ArrayList<>();
    static int id_counter = 0;

    public boolean loadJar(String path, int existingID, boolean isRelativePath) {

        //String pathToJar = "src/main/resources/components/" + fileName;
        Enumeration<JarEntry> jarEntries = null;
        URLClassLoader classLoader = null;

        String componentPath;

        if (isRelativePath) {
            componentPath = System.getProperty("user.dir") + "\\" + path;
        }
        else {
            componentPath = path;
        }

        try {
            JarFile jarFile = new JarFile(componentPath);
            jarEntries = jarFile.entries();

            URL[] urls = new URL[]{new URL("jar:file:" + componentPath + "!/")};
            classLoader = URLClassLoader.newInstance(urls);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return false;
        }

        Method startMethod = null;
        Method endMethod = null;
        Class<?> componentClass = null;

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }

            String className = getNameFromJarEntry(jarEntry);

            Class<?> loadedClass = loadClassWithClassloader(classLoader, className);

            Method localStartMethod = getMethodsAnnotatedWith(loadedClass, StartMethodAnnotation.class).stream().findFirst().orElse(null);
            Method localEndMethod = getMethodsAnnotatedWith(loadedClass, StopMethodAnnotation.class).stream().findFirst().orElse(null);

            if (localStartMethod != null) {
                startMethod = localStartMethod;
                componentClass = startMethod.getDeclaringClass();
            }

            if (localEndMethod != null) {
                endMethod = localEndMethod;
            }

            if (startMethod != null && endMethod != null) break;
        }

        if (componentClass == null) {
            System.out.println("error: Couldn't find start or end class");
            return false;
        }

        // find possible load-Method
        Method loadMethod = getMethodsAnnotatedWith(componentClass, LoadMethodAnnotation.class).stream().findFirst().orElse(null);

        // Create new Component and add it to list
        int id;

        if (existingID == -1) {
            id = id_counter;
        }
        else {
            id = existingID;
            id_counter = existingID;
        }

        Component component = new Component(id, componentPath, classLoader, startMethod, endMethod, loadMethod, componentClass);
        _components.add(component);

        System.out.println("Deployed Component: " + _components.get(_components.size()-1).toString());

        id_counter += 1;

        return true;
    }

    public void load(String saveFilePath) {

        if (!_components.isEmpty()) {
            System.out.println("Error: Components are not empty");
            return;
        }

        String fullPath = System.getProperty("user.dir") + "\\" + saveFilePath;

        String content = "{}";
        // From: https://www.digitalocean.com/community/tutorials/java-read-file-to-string
        try {
            content = new String(Files.readAllBytes(Paths.get(fullPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(content);
        JSONObject obj = (JSONObject)JSONValue.parse(content);

        Set<Map.Entry<?, ?>> entrySet = obj.entrySet();

        // Step to create a new set of Entry<Integer, String>
        List<Map.Entry<Integer, String>> entryList = new ArrayList<>();

        for (Map.Entry<?, ?> rawEntry : entrySet) {
            Object key = rawEntry.getKey();
            Object value = rawEntry.getValue();
            Integer integerKey = null;

            // Attempt to cast or convert the key to Integer
            if (key instanceof Integer) {
                integerKey = (Integer) key; // Key is already an Integer
            } else if (key instanceof String) {
                try {
                    // Try to parse the String key to Integer
                    integerKey = Integer.parseInt((String) key);
                } catch (NumberFormatException e) {
                    System.out.println("Cannot convert key to Integer: " + key);
                }
            } else {
                System.out.println("Unsupported key type for conversion: " + key);
            }

            if (integerKey != null  && value instanceof String) {
                // Safe to cast as we have checked the types
                Map.Entry<Integer, String> castedEntry = new AbstractMap.SimpleEntry<>(integerKey, (String) value);
                entryList.add(castedEntry);
            } else {
                System.out.println("Skipping entry due to incompatible types: " + rawEntry);
            }
        }

        entryList.sort(Comparator.comparingInt(Map.Entry::getKey));

        // Print out the successfully casted entries
        for (Map.Entry<Integer, String> entry : entryList) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        for (int i = 0; i < entryList.size(); i++){
            loadJar(entryList.get(i).getValue(), entryList.get(i).getKey(), false);
        }
    }

    public String getNameFromJarEntry(JarEntry jarEntry) {
        String className = jarEntry.getName();
        // -6 to remove .class ending
        className = className.substring(0, className.length() - 6);
        return className.replace('/', '.');
    }

    public Class<?> loadClassWithClassloader(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // from https://stackoverflow.com/questions/6593597/java-seek-a-method-with-specific-annotation-and-its-annotation-element
    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;

        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
        for (final Method method : klass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }

        return methods;
    }

    public boolean startComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            System.out.println("error: Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.SLEEP)){
            System.out.println("error: Component is not in sleep-state");
            return false;
        }

        component._thread = new Thread(component);
        component._thread.start();
        component._componentState = ComponentState.ACTIVE;

        return true;
    }

    public boolean stopComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            System.out.println("error: Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.ACTIVE)){
            System.out.println("error: Component is not in active-state");
            return false;
        }

        if (!(component._thread.isAlive())){
            System.out.println("error: Components Thread is not alive");
        }

        component.stop();
        component._componentState = ComponentState.SLEEP;

        return true;
    }

    public boolean deleteComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            System.out.println("error: Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.SLEEP)){
            System.out.println("error: Component is not in sleep-state");
            return false;
        }

        _components.remove(component);

        return true;
    }

    public void stressComponent(){
        // as this is a loadbalancer, find all applicable components and choose one randomly
        ArrayList<Component> loadComponents = new ArrayList<>();
        for (Component component : _components) {
            if (component.get_loadMethod() == null || component.get_componentState() == ComponentState.SLEEP) continue;
            loadComponents.add(component);
        }

        if (loadComponents.isEmpty()){
            System.out.println("error: No component available");
            return;
        }

        Component chosenComponent = loadComponents.get((int)(Math.random() * loadComponents.size()));
        chosenComponent.processLoad();
    }

    public String getComponentStatus(String componentId) {
        String status = "";

        if (componentId.isEmpty()){
            status = getComponentStatusAll();
        }
        else {
            try{
                status = getComponentStatusById(Integer.parseInt(componentId));
            }
            catch (Exception e){
                System.out.println("error: Enter an ID, not a name.");
            }
        }

        if (status.isEmpty()){
            return "No Component Found";
        }

        return status;
    }

    public String getComponentStatusById(int componentId) {
        for (Component component : _components) {
            if (component._id == componentId){
                return component.toString();
            }
        }
        return "";
    }

    public String getComponentStatusAll() {
        String status = "";
        for (Component component : _components) {
            status = status.concat(component.toString() + "\n");
        }
        return status;
    }

    public List<Component> getComponents() {
        return _components;
    }
}
