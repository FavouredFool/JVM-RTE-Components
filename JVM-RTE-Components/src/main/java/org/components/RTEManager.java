package org.components;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.logging.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RTEManager {

    ComponentManager _componentManager;
    JSONManager _jsonManager;
    Logger logger = new Logger();

    public RTEManager(ComponentManager componentManager, JSONManager jsonManager) {
        _componentManager = componentManager;
        _jsonManager = jsonManager;
    }

    public void loadComponentsFromJson(String saveFilePath) {
        if (!_componentManager.getComponents().isEmpty()) {
            logger.printMessageError("Components are not empty");
            return;
        }

        String jsonContent = _jsonManager.findJsonContent(saveFilePath);
        JSONObject obj = (JSONObject) JSONValue.parse(jsonContent);

        List<Map.Entry<Integer, String>> entryList = _jsonManager.readEntriesFromJsonContent(obj.entrySet());
        entryList.sort(Comparator.comparingInt(Map.Entry::getKey));

        _componentManager.loadComponents(entryList);
    }

    public void writeComponentsToJson(boolean withTimeStamp) {
        _jsonManager.writeJson(_componentManager.getComponents(), withTimeStamp);
    }

    public ComponentManager get_componentManager() {
        return _componentManager;
    }
}
