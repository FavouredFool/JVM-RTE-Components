import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineReader {

    RTEManager _rteManager;


    public CommandLineReader(RTEManager rteManager){
        _rteManager = rteManager;
    }

    public void ReadFromCommandLine() {

        WriteTutorial();

        // https://www.innoq.com/de/articles/2017/10/java-command-line-interfaces/
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line = null;
            while (true) {
                line = in.readLine();
                if (line == null) break;
                if (line.equals("exit")) break;

                InterpretCommand(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void InterpretCommand(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 2 || parts.length > 3) return;

        String verb = parts[1];
        String file = "";
        if (parts.length == 3) file = parts[2];

        switch (parts[0]) {
            case "rte":
                InterpretRTECommand(verb, file);
                break;
            case "component":
                InterpretComponentCommand(verb, file);
                break;
        }
    }

    void InterpretRTECommand(String verb, String file) {
        switch (verb) {
            case "start":
                break;
            case "stop":
                break;
            default: return;
        }

        if (Main.Debug) System.out.println("rte: " + verb + " " + file);
    }

    void InterpretComponentCommand(String verb,  String file) {
        if (file.equals("")) return;

        switch (verb) {
            case "deploy":
                _rteManager.Deploy(file);
                break;
            case "start":
                break;
            case "stop":
                break;
            case "status":
                _rteManager.Status(file);
                break;
            case "delete":
                break;
            default: return;
        }

        if (Main.Debug) System.out.println("component: " + verb + " " + file);
    }

    void WriteTutorial() {
        System.out.println("--- This is a component-based Java-Runtime-Environment. ---");
        System.out.println("--- All components that can be deployed are already provided in a separate folder as .jar files. ---");
        System.out.println();
        System.out.println("--- Commands:");
        System.out.println("\"rte start\" --- starts the Runtime Environment.");
        System.out.println("\"rte stop\" --- stops the Runtime Environment.");
        System.out.println("\"component deploy [path to .jar]\" --- deploys the component from a local folder.");
        System.out.println("\"component start [component.jar]\" --- starts the component.");
        System.out.println("\"component stop [component.jar]\" --- stops the component.");
        System.out.println("\"component status [component.jar]\" --- logs the component's status.");
        System.out.println("\"component delete [component.jar]\" --- deletes the component.");
        System.out.println();
    }
}
