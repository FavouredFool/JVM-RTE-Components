package org.components;

import org.components.commands.*;
import org.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineReader {

    RTEManager _rteManager;
    Logger _logger = new Logger();

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
                _logger.printMessageInfo("Power down Runtime Environment. Goodbye.");
                System.exit(0);
                break;
        }
    }

    void interpretComponentCommand(String verb, String inputString) {

        Command inputCommand = switch (verb) {
            case "deploy" -> new DeployCommand(_rteManager);
            case "start" -> new StartCommand(_rteManager);
            case "stop" -> new StopCommand(_rteManager);
            case "status" -> new StatusCommand(_rteManager);
            case "delete" -> new DeleteCommand(_rteManager);
            case "save" -> new SaveCommand(_rteManager);
            case "load" -> new LoadCommand(_rteManager);
            case "stress" -> new StressCommand(_rteManager);
            default -> new NullCommand(_rteManager);
        };

        inputCommand.execute(inputString);
    }

    void writeTutorial() {
        _logger.printMessageInfo("This is Runtime-Environment for component deployment.");
        _logger.printMessageInfo("Supported components are HotelComponent, GreetingComponent, and LoadComponent (for load balancing).");
        _logger.printEmpty();
        _logger.printMessageInfo("Commands");
        _logger.printEmpty();
        _logger.printMessageInfo("\"component deploy [relative path to .jar]\" --- deploys the component from a local folder.");
        _logger.printMessageInfo("\"component status\" --- logs every component's status.");
        _logger.printMessageInfo("\"component status [componentID]\" --- logs the component's status.");
        _logger.printMessageInfo("\"component start [componentID]\" --- starts the component.");
        _logger.printMessageInfo("\"component stop [componentID]\" --- stops the component.");
        _logger.printMessageInfo("\"component delete [componentID]\" --- deletes the component.");
        _logger.printMessageInfo("\"component save\" --- saves the component configuration.");
        _logger.printMessageInfo("\"component load [jsonPath]\" --- loads a configuration from a json file.");
        _logger.printMessageInfo("\"component stress\" --- attempts to put stress on a loadComponent if available.");
        _logger.printMessageInfo("\"rte stop\" --- stops the Runtime Environment.");
    }
}
