

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

    public void Start(String componentId){
        boolean success = _componentManager.StartComponent(Integer.parseInt(componentId));

        if (success) {
            System.out.println("Component with ID " + componentId + " successfully started.");
        }
        else {
            System.out.println("Component with ID " + componentId + " could not be started.");
        }
    }

    public void Status(String componentId){
        if (componentId.equals("")){
            System.out.println("Status for all Components:");
        }
        else {
            System.out.println("Status for Component with ID " + componentId + ":");
        }

        System.out.println(_componentManager.GetComponentStatus(componentId));
    }
}
