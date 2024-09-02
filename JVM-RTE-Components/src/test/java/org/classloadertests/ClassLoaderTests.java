package org.classloadertests;

import org.junit.jupiter.api.*;
import org.components.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.junit.jupiter.api.Assertions.fail;

class ClassLoaderTests {

    @Test
    void testComponentLoading() {
        // Tests if components could be loaded
        ComponentManager componentManager = new ComponentManager();
        Assertions.assertTrue(componentManager.loadJar("src/main/resources/tests/GreetingComponent.jar", -1, true));
        Assertions.assertTrue(componentManager.loadJar("src/main/resources/tests/HotelComponent.jar", -1, true));
    }


    @Test
    void testComponentParameters() {
        // Tests if the loaded Components have their required parameters set
        String path = "src/main/resources/tests/HotelComponent.jar";
        ComponentManager componentManager = new ComponentManager();
        Assertions.assertTrue(componentManager.loadJar(path, -1, true));

        List<Component> components = componentManager.getComponents();
        Assertions.assertFalse(components.isEmpty());

        Component component = components.get(0);
        Assertions.assertNotNull(component);
        Assertions.assertEquals(0, component.get_id());
        Assertions.assertNotNull(component.get_startMethod());
        Assertions.assertNotNull(component.get_endMethod());
        Assertions.assertNotNull(component.get_componentClass());
    }


    @Test
    void testIsolatedLoading() {
        // Tests if components can be uniquely identified through their classloader
        ComponentManager componentManager = new ComponentManager();
        String componentPath = "src/main/resources/tests/GreetingComponent.jar";

        // Load Component
        JarFile jarFile = null;
        URL[] urls = null;
        try {
            jarFile = new JarFile(componentPath);
            urls = new URL[]{new URL("jar:file:" + componentPath + "!/")};
        } catch (IOException e) {
            fail();
        }
        Enumeration<JarEntry> jarEntries = jarFile.entries();

        // Create two different Classloaders
        ClassLoader classLoader1 = URLClassLoader.newInstance(urls);
        ClassLoader classLoader2 = URLClassLoader.newInstance(urls);

        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }

            String className = componentManager.getNameFromJarEntry(jarEntry);

            // Load every class with both classloaders and compare them
            Class<?> loadedClass1 = componentManager.loadClassWithClassloader(classLoader1, className);
            Class<?> loadedClass2 = componentManager.loadClassWithClassloader(classLoader2, className);

            // if they're equal, they cant be uniquely identified through the classloaders -> failure
            Assertions.assertNotSame(loadedClass1, loadedClass2);
        }
    }

}