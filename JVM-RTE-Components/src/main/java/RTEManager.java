

public class RTEManager {

    ComponentManager _componentManager;

    public RTEManager() {
        _componentManager = new ComponentManager();
    }

    public void Deploy(String componentName) {
        if (_componentManager.LoadJar(componentName)){
            System.out.println(componentName + " successfully loaded.");
        }
        else {
            System.out.println(componentName + " could not be loaded.");
        }

    }

    public void Start(String componentName){
        boolean success = _componentManager.StartComponent(componentName);

        if (success) {
            System.out.println(componentName + " successfully started.");
        }
        else {
            System.out.println(componentName + " could not be started.");
        }
    }

    public void Status(String componentName){
        System.out.println("Status for " + componentName + ":");
        System.out.println(_componentManager.GetComponentStatus(componentName));
    }
}
