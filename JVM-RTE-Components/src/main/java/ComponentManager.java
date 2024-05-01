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

    public boolean LoadJar(String componentName) {

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
            System.out.println("error: " + e.getMessage());
            return false;
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

                Method localStartMethod = getMethodsAnnotatedWith(loadedClass, StartAnnotation.class).stream().findFirst().orElse(null);
                Method localEndMethod = getMethodsAnnotatedWith(loadedClass, StopAnnotation.class).stream().findFirst().orElse(null);

                if (localStartMethod != null) {
                    startMethod = localStartMethod;
                }

                if (localEndMethod != null) {
                    endMethod = localEndMethod;
                }

            } catch (ClassNotFoundException ex) {
                System.out.println("error: " + ex.getMessage());
                return false;
            }
        }

        if (startMethod == null || endMethod == null) {
            System.out.println("error: Couldn't find start or end method");
            return false;
        }

        // Create new Component and add it to list
        _components.add(new Component(_components.size(), componentName, classLoader, startMethod, endMethod));
        return true;
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
