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
        if (parts.length == 3 && !parts[2].equals("*")) file = parts[2];

        switch (parts[0]) {
            case "component":
                InterpretComponentCommand(verb, file);
                break;
        }
    }

    void InterpretComponentCommand(String verb,  String file) {

        switch (verb) {
            case "deploy":
                _rteManager.Deploy(file);
                break;
            case "start":
                _rteManager.Start(file);
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
    }

    void WriteTutorial() {
        System.out.println("--- This is a component-based Java-Runtime-Environment. ---");
        System.out.println();
        System.out.println("--- Commands:");
        System.out.println("\"rte stop\" --- stops the application.");
        System.out.println("\"component deploy [path to .jar]\" --- deploys the component from a local folder.");
        System.out.println("\"component status\" --- logs every component's status.");
        System.out.println("\"component status [componentID]\" --- logs the component's status.");
        System.out.println("\"component start [componentID]\" --- starts the component.");
        System.out.println("\"component stop [componentID]\" --- stops the component.");
        System.out.println("\"component delete [componentID]\" --- deletes the component.");
        System.out.println();
    }
}
