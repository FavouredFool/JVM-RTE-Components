package org.components;

import org.logging.Logger;

public class Main {

    public static boolean Debug;

    public static void main(String[] args) {
        for (String arg : args) {
            switch(arg) {
                case "--debug":
                case "-d":
                    Debug = true;
                    break;
            }
        }

        ComponentManager componentManager = new ComponentManager();
        JSONManager jsonManager = new JSONManager();
        RTEManager rteManager = new RTEManager(componentManager, jsonManager);
        CommandLineReader reader = new CommandLineReader(rteManager);

        rteManager.writeComponentsToJson(false);

        reader.readFromCommandLine();
    }

}
