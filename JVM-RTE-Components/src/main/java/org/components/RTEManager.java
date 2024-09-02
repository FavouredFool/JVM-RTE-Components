package org.components;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RTEManager {

    ComponentManager _componentManager;
    static String saveFileName = "savedConfiguration";

    public RTEManager() {
        _componentManager = new ComponentManager();
    }

    public void deploy(String componentPath) {
        if (_componentManager.loadJar(componentPath, -1, true)){
            System.out.println(componentPath + " successfully loaded.");
            writeJson(false);
        }
        else {
            System.out.println(componentPath + " could not be loaded.");
        }
    }

    public void load(String saveFilePath){
        _componentManager.load(saveFilePath);
    }

    public void save(){
        writeJson(true);
        System.out.println("Saved configuration");
    }

    public void writeJson(boolean withTimeStamp){

        //From: https://stackoverflow.com/questions/23068676/how-to-get-current-timestamp-in-string-format-in-java-yyyy-mm-dd-hh-mm-ss
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        // Format the current date and time
        String formattedNow = now.format(formatter);

        // From: https://www.tutorialspoint.com/how-to-write-create-a-json-file-using-java

        JSONObject jsonObject = new JSONObject();

        for(Component component : _componentManager.getComponents()){
            jsonObject.put(component.get_id(), component.get_path());
        }

        FileWriter file = null;
        try {
            if (withTimeStamp){
                file = new FileWriter(System.getProperty("user.dir") + "/[" + formattedNow + "]_" + saveFileName + ".json");
            }
            else {
                file = new FileWriter(System.getProperty("user.dir") + "/" + saveFileName + ".json");
            }

            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            writeJson(false);
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
