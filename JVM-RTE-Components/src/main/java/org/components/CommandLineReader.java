package org.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineReader {

    RTEManager _rteManager;


    public CommandLineReader(RTEManager rteManager){
        _rteManager = rteManager;
    }

    public void readFromCommandLine() {

        writeTutorial();

        // https://www.innoq.com/de/articles/2017/10/java-command-line-interfaces/
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line = null;
            while (true) {
                line = in.readLine();
                if (line == null) break;
                if (line.equals("exit")) break;

                interpretCommand(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void interpretCommand(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 2 || parts.length > 3) return;

        String verb = parts[1];
        String file = "";
        if (parts.length == 3 && !parts[2].equals("*")) file = parts[2];

        switch (parts[0]) {
            case "rte":
                interpretRTECommands(verb, file);
                break;
            case "component":
                interpretComponentCommand(verb, file);
                break;
        }
    }

    void interpretRTECommands(String verb, String file) {
        switch (verb) {
            case "stop":
                System.out.println("--- Power down Runtime Environment. Goodbye. ---");
                System.exit(0);
                break;
        }
    }

    void interpretComponentCommand(String verb, String file) {

        switch (verb) {
            case "deploy":
                _rteManager.deploy(file);
                break;
            case "start":
                _rteManager.start(file);
                break;
            case "stop":
                _rteManager.stop(file);
                break;
            case "status":
                _rteManager.status(file);
                break;
            case "delete":
                _rteManager.delete(file);
                break;
            default: return;
        }
    }

    void writeTutorial() {
        System.out.println("--- This is Runtime-Environment for component deployment. ---");
        System.out.println("--- Active Components print to console every 10 seconds. ---");
        System.out.println();
        System.out.println("--- Commands: ---");
        System.out.println();
        System.out.println("\"component deploy [path to .jar]\" --- deploys the component from a local folder.");
        System.out.println("(The path can be both an absolute path like \"D:/HotelComponent.jar\" or a relative path like \"../HotelComponent.jar\").");
        System.out.println("\"component status\" --- logs every component's status.");
        System.out.println("\"component status [componentID]\" --- logs the component's status.");
        System.out.println("\"component start [componentID]\" --- starts the component.");
        System.out.println("\"component stop [componentID]\" --- stops the component.");
        System.out.println("\"component delete [componentID]\" --- deletes the component.");
        System.out.println("\"rte stop\" --- stops the Runtime Environment.");
    }
}
