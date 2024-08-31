package org.components;

public class RTEManager {

    ComponentManager _componentManager;

    public RTEManager() {
        _componentManager = new ComponentManager();
    }

    public void deploy(String componentPath) {
        if (_componentManager.loadJar(componentPath)){
            System.out.println(componentPath + " successfully loaded.");
        }
        else {
            System.out.println(componentPath + " could not be loaded.");
        }

    }

    public void start(String componentId){
        boolean success;
        try {
            success = _componentManager.startComponent(Integer.parseInt(componentId));
        }
        catch(Exception e) {
            System.out.println("error: Enter an ID, not a name.");
            success = false;
        }


        if (success) {
            System.out.println("Component with ID " + componentId + " successfully started.");
        }
        else {
            System.out.println("Component with ID " + componentId + " could not be started.");
        }
    }

    public void stop(String componentId){
        boolean success;
        try {
            success = _componentManager.stopComponent(Integer.parseInt(componentId));
        }
        catch(Exception e) {
            System.out.println("error: Enter an ID, not a name.");
            success = false;
        }

        if (success) {
            System.out.println("Component with ID " + componentId + " successfully stopped.");
        }
        else {
            System.out.println("Component with ID " + componentId + " could not be stopped.");
        }
    }

    public void delete(String componentId){
        boolean success;
        try {
            success = _componentManager.deleteComponent(Integer.parseInt(componentId));
        }
        catch(Exception e) {
            System.out.println("error: Enter an ID, not a name.");
            success = false;
        }

        if (success) {
            System.out.println("Component with ID " + componentId + " successfully deleted.");
        }
        else {
            System.out.println("Component with ID " + componentId + " could not be deleted.");
        }
    }

    public void status(String componentId){
        if (componentId.isEmpty()){
            System.out.println("Status for all Components:");
        }
        else {
            System.out.println("Status for Component with ID " + componentId + ":");
        }

        System.out.println(_componentManager.getComponentStatus(componentId));
    }
}
