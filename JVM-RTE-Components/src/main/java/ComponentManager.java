import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

public class ComponentManager {

    List<Component> _components = new ArrayList<>();

    public void LoadJar(String componentName) {

        String fileName = componentName + ".jar";

        String pathToJar = "src/main/resources/components/" + fileName;
        Enumeration<JarEntry> jarEntries = null;
        URLClassLoader classLoader = null;

        try {
            JarFile jarFile = new JarFile(pathToJar);
            jarEntries = jarFile.entries();

            URL[] urls = new URL[]{new URL("jar:file:" + pathToJar + "!/")};
            classLoader = URLClassLoader.newInstance(urls);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            //e.printStackTrace();
            System.out.println("error");
            return;
        }

        Method startMethod = null;
        Method endMethod = null;

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }

            String className = jarEntry.getName();
            // -6 to remove .class ending
            className = className.substring(0, className.length() - 6);
            className = className.replace('/', '.');

            try {
                Class<?> loadedClass = classLoader.loadClass(className);

                for (Method method : loadedClass.getDeclaredMethods()){

                    if (startMethod != null && endMethod != null) break;

                    for (Annotation annotation : method.getDeclaredAnnotations()){
                        if (annotation instanceof StartAnnotation) {
                            System.out.println("foundStart");
                            startMethod = method;
                        }
                        if (annotation instanceof StartAnnotation) {
                            System.out.println("foundEnd");
                            endMethod = method;
                        }
                    }
                }

            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (startMethod == null || endMethod == null) {
            System.out.println("Couldn't find start or end method");
        }

        // Create new Component and add it to list
        _components.add(new Component(_components.size(), componentName, classLoader, startMethod, endMethod));
    }

    public boolean StartComponent(String componentName) {
        Component component = _components.stream().filter(e -> e._name.equals(componentName)).findFirst().orElse(null);

        if (component == null) {
            return false;
        }

        return true;
    }

    public String GetComponentStatus(String componentName) {
        String status;

        if (componentName.equals("")){
            status = GetComponentStatusAll();
        }
        else {
            status = GetComponentStatusByName(componentName);
        }

        if (status.equals("")){
            return "No Component Found";
        }

        return status;
    }

    public String GetComponentStatusByName(String componentName) {
        for (Component component : _components) {
            if (component._name.equals(componentName)){
                return component.GetStatus();
            }
        }
        return "";
    }

    public String GetComponentStatusAll() {
        String status = "";
        for (Component component : _components) {
            status = status.concat(component.GetStatus() + "\n");
        }
        return status;
    }
}
