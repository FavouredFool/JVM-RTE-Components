package org.components;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import org.componentannotations.*;

public class ComponentManager {

    List<Component> _components = new ArrayList<>();

    public boolean LoadJar(String componentPath) {

        //String pathToJar = "src/main/resources/components/" + fileName;
        Enumeration<JarEntry> jarEntries = null;
        URLClassLoader classLoader = null;

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
        Class<?> startClass = null;
        Class<?> endClass = null;

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

                Method localStartMethod = getMethodsAnnotatedWith(loadedClass, StartMethodAnnotation.class).stream().findFirst().orElse(null);
                Method localEndMethod = getMethodsAnnotatedWith(loadedClass, StopMethodAnnotation.class).stream().findFirst().orElse(null);
                Class<?> localStartClass =  loadedClass.isAnnotationPresent(StartClassAnnotation.class) ?  loadedClass : null;
                Class<?> localEndClass =  loadedClass.isAnnotationPresent(StartClassAnnotation.class) ?  loadedClass : null;

                if (localStartMethod != null) {
                    startMethod = localStartMethod;
                }

                if (localEndMethod != null) {
                    endMethod = localEndMethod;
                }

                if (localStartClass != null) {
                    startClass = localStartClass;
                }

                if (localEndClass != null) {
                    endClass = localEndClass;
                }

            } catch (ClassNotFoundException ex) {
                System.out.println("error: " + ex.getMessage());
                return false;
            }
        }

        if (startClass == null || endClass == null) {
            System.out.println("error: Couldn't find start or end class");
            return false;
        }

        if (startMethod == null || endMethod == null) {
            System.out.println("error: Couldn't find start or end method");
            return false;
        }

        // Create new Component and add it to list
        _components.add(new Component(_components.size(), componentPath, classLoader, startMethod, endMethod, startClass, endClass));
        System.out.println("Deployed Component: " + _components.get(_components.size()-1).toString());
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

    public boolean StartComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            System.out.println("error: Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.SLEEP)){
            System.out.println("error: Component is not in sleep-state");
            return false;
        }

        component._componentState = ComponentState.START;

        Object test = null;
        try {
            test = component._startClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            component._startMethod.invoke(test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean StopComponent(int componentId) {
        Component component = _components.stream().filter(e -> e._id == componentId).findFirst().orElse(null);

        if (component == null) {
            System.out.println("error: Couldn't find component with id " + componentId);
            return false;
        }

        if (!(component._componentState == ComponentState.ACTIVE)){
            System.out.println("error: Component is not in active-state");
            return false;
        }

        component._componentState = ComponentState.STOP;

        Object test = null;
        try {
            test = component._endClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            component._endMethod.invoke(test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean DeleteComponent(int componentId) {
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

    public String GetComponentStatus(String componentId) {
        String status = "";

        if (componentId.isEmpty()){
            status = GetComponentStatusAll();
        }
        else {
            try{
                status = GetComponentStatusById(Integer.parseInt(componentId));
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

    public String GetComponentStatusById(int componentId) {
        for (Component component : _components) {
            if (component._id == componentId){
                return component.toString();
            }
        }
        return "";
    }

    public String GetComponentStatusAll() {
        String status = "";
        for (Component component : _components) {
            status = status.concat(component.toString() + "\n");
        }
        return status;
    }
}
