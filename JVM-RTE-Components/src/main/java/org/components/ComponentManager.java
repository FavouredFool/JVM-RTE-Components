package org.components;

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
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.logging.Logger;

public class ComponentManager {

    List<Component> _components = new ArrayList<>();
    static int _id_counter = 0;
    Logger _logger = new Logger();

    public boolean loadJar(String path, int existingID, boolean isRelativePath) {

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
            _logger.printMessageError(e.getMessage());
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
            _logger.printMessageError("Couldn't find start or end class");
            return false;
        }

        // find possible load-Method
        Method loadMethod = getMethodsAnnotatedWith(componentClass, LoadMethodAnnotation.class).stream().findFirst().orElse(null);

        // Create new Component and add it to list
        _components.add(buildComponent(existingID, componentPath, classLoader, startMethod, endMethod, loadMethod, componentClass));

        return true;
    }

    public String getNameFromJarEntry(JarEntry jarEntry) {
        String className = jarEntry.getName();
        // -6 to remove .class ending
        className = className.substring(0, className.length() - 6);
        return className.replace('/', '.');
    }

    public Component buildComponent(int existingID, String componentPath, URLClassLoader classLoader, Method startMethod, Method endMethod, Method loadMethod, Class<?> componentClass) {
        int id;

        if (existingID == -1) {
            id = _id_counter;
        }
        else {
            id = existingID;
            _id_counter = existingID;
        }

        _id_counter += 1;

        Component newComponent = new Component(id, componentPath, classLoader, startMethod, endMethod, loadMethod, componentClass);
        _logger.printMessageInfo("Deployed Component: " + newComponent);

        return newComponent;
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

    public void loadComponents(List<Map.Entry<Integer, String>> entryList){
        for (int i = 0; i < entryList.size(); i++){
            loadJar(entryList.get(i).getValue(), entryList.get(i).getKey(), false);
        }
    }

    public boolean startComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            _logger.printMessageError("Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.SLEEP)){
            _logger.printMessageError("Component is not in sleep-state");
            return false;
        }

        component.start();

        return true;
    }

    public boolean stopComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            _logger.printMessageError("Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.ACTIVE)){
            _logger.printMessageError("Component is not in active-state");
            return false;
        }

        if (!(component._thread.isAlive())){
            _logger.printMessageError("Components Thread is not alive");
        }

        component.stop();

        return true;
    }

    public boolean deleteComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            _logger.printMessageError("Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.SLEEP)){
            _logger.printMessageError("Component is not in sleep-state");
            return false;
        }

        _components.remove(component);

        return true;
    }

    public void stressComponent(int stress){
        // as this is a loadbalancer, find all applicable components and choose one randomly
        ArrayList<Component> loadComponents = new ArrayList<>();
        for (Component component : _components) {
            if (component.get_loadMethod() == null || component.get_componentState() == ComponentState.SLEEP) continue;
            loadComponents.add(component);
        }

        if (loadComponents.isEmpty()){
            _logger.printMessageError("No component available");
            return;
        }

        Component chosenComponent = loadComponents.get((int)(Math.random() * loadComponents.size()));
        chosenComponent.processLoad(stress);
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
                _logger.printMessageError("Enter an ID, not a name.");
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
