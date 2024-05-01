package org.components;

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

        RTEManager rteManager = new RTEManager();
        CommandLineReader reader = new CommandLineReader(rteManager);

        // this blocks the main Thread forever
        reader.ReadFromCommandLine();
    }

}
