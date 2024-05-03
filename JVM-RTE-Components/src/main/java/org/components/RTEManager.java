package org.components;

public class RTEManager {

    ComponentManager _componentManager;

    public RTEManager() {
        _componentManager = new ComponentManager();
    }

    public void Deploy(String componentPath) {
        if (_componentManager.LoadJar(componentPath)){
            System.out.println(componentPath + " successfully loaded.");
        }
        else {
            System.out.println(componentPath + " could not be loaded.");
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

    public void Stop(String componentId){
        boolean success = _componentManager.StopComponent(Integer.parseInt(componentId));

        if (success) {
            System.out.println("Component with ID " + componentId + " successfully stopped.");
        }
        else {
            System.out.println("Component with ID " + componentId + " could not be stopped.");
        }
    }

    public void Delete(String componentId){
        boolean success = _componentManager.DeleteComponent(Integer.parseInt(componentId));

        if (success) {
            System.out.println("Component with ID " + componentId + " successfully deleted.");
        }
        else {
            System.out.println("Component with ID " + componentId + " could not be deleted.");
        }
    }

    public void Status(String componentId){
        if (componentId.isEmpty()){
            System.out.println("Status for all Components:");
        }
        else {
            System.out.println("Status for Component with ID " + componentId + ":");
        }

        System.out.println(_componentManager.GetComponentStatus(componentId));
    }
}
