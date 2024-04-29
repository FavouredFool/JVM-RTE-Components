

public class RTEManager {

    ComponentManager _loader;

    public RTEManager() {
        _loader = new ComponentManager();
    }

    public void Deploy(String componentName) {
        _loader.LoadJar(componentName);
        System.out.println(componentName + " successfully loaded.");
    }

    public void Status(String componentName){
        System.out.println("Status for " + componentName + ":");
        System.out.println(_loader.GetComponentStatus(componentName));
    }
}
