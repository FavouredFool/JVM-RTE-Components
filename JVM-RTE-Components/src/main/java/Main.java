import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, Java");

        String pathToJar = "src/main/resources/components/Task1-1.0-SNAPSHOT.jar";

        Enumeration<JarEntry> jarEntries = null;
        URLClassLoader classLoader = null;

        try {
            JarFile jarFile = new JarFile(pathToJar);
            jarEntries = jarFile.entries();

            URL[] urls = new URL[]{ new URL("jar:file:" + pathToJar + "!/") };
            classLoader = URLClassLoader.newInstance(urls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = jarEntry.getName().substring(0,jarEntry.getName().length()-6);
            className = className.replace('/', '.');

            try {
                Class<?> classToLoad = classLoader.loadClass(className);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        }

    }

}
