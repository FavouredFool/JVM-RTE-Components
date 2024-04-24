import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

public class ComponentLoader {

    public void LoadJar(String jarFileName) {

        jarFileName = "Task1-1.0-SNAPSHOT.jar";

        String pathToJar = "src/main/resources/components/" + jarFileName;

        Enumeration<JarEntry> jarEntries = null;
        URLClassLoader classLoader = null;

        try {
            JarFile jarFile = new JarFile(pathToJar);
            jarEntries = jarFile.entries();

            URL[] urls = new URL[]{new URL("jar:file:" + pathToJar + "!/")};
            classLoader = URLClassLoader.newInstance(urls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
                System.out.println(classLoader.getResourceAsStream(loadedClass.getName().replace('.', '/') + ".class"));
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
