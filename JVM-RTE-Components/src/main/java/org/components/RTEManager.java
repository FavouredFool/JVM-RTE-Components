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

    public ComponentManager get_componentManager() {
        return _componentManager;
    }
}
