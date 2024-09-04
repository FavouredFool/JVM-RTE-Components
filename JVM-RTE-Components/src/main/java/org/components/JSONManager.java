package org.components;

import org.json.simple.JSONObject;
import org.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JSONManager {

    static String _saveFileName = "savedConfiguration";
    Logger _logger = new Logger();

    public void writeJson(List<Component> components, boolean withTimeStamp){

        //From: https://stackoverflow.com/questions/23068676/how-to-get-current-timestamp-in-string-format-in-java-yyyy-mm-dd-hh-mm-ss
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        // Format the current date and time
        String formattedNow = now.format(formatter);

        // From: https://www.tutorialspoint.com/how-to-write-create-a-json-file-using-java

        JSONObject jsonObject = new JSONObject();

        for(Component component : components){
            jsonObject.put(component.get_id(), component.get_path());
        }

        FileWriter file = null;
        try {
            if (withTimeStamp){
                file = new FileWriter(System.getProperty("user.dir") + "/[" + formattedNow + "]_" + _saveFileName + ".json");
            }
            else {
                file = new FileWriter(System.getProperty("user.dir") + "/" + _saveFileName + ".json");
            }

            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String findJsonContent(String saveFilePath) {
        String fullPath = System.getProperty("user.dir") + "\\" + saveFilePath;

        String content = "{}";
        // From: https://www.digitalocean.com/community/tutorials/java-read-file-to-string
        try {
            content = new String(Files.readAllBytes(Paths.get(fullPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }

    public List<Map.Entry<Integer, String>> readEntriesFromJsonContent(Set<Map.Entry<?, ?>> entrySet) {
        // Step to create a new set of Entry<Integer, String>
        List<Map.Entry<Integer, String>> entryList = new ArrayList<>();

        for (Map.Entry<?, ?> rawEntry : entrySet) {
            Object key = rawEntry.getKey();
            Object value = rawEntry.getValue();
            Integer integerKey = null;

            // Attempt to cast or convert the key to Integer
            if (key instanceof Integer) {
                integerKey = (Integer) key; // Key is already an Integer
            } else if (key instanceof String) {
                try {
                    // Try to parse the String key to Integer
                    integerKey = Integer.parseInt((String) key);
                } catch (NumberFormatException e) {
                    _logger.printMessageInfo("Cannot convert key to Integer: " + key);
                }
            } else {
                _logger.printMessageInfo("Unsupported key type for conversion: " + key);
            }

            if (integerKey != null  && value instanceof String) {
                // Safe to cast as we have checked the types
                Map.Entry<Integer, String> castedEntry = new AbstractMap.SimpleEntry<>(integerKey, (String) value);
                entryList.add(castedEntry);
            } else {
                _logger.printMessageInfo("Skipping entry due to incompatible types: " + rawEntry);
            }
        }
        return entryList;
    }

}
