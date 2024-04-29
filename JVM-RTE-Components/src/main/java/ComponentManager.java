import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.UUID;

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
                classLoader.loadClass(className);
                //classLoader.
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Create new Component and add it to list
        _components.add(new Component(componentName, classLoader));
    }

    public String GetComponentStatus(String componentName) {
        for (Component component : _components) {
            if (component._name.equals(componentName)){
                return component.GetStatus();
            }
        }

        return "No Component Found";
    }
}
