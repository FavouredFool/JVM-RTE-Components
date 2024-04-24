import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineReader {

    public void ReadFromCommandLine() {

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
        if (Main.Debug) System.out.println("rte: " + verb + " " + file);
    }

    void InterpretComponentCommand(String verb,  String file) {
        if (Main.Debug) System.out.println("component: " + verb + " " + file);
    }
}
