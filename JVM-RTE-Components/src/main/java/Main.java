

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

        CommandLineReader reader = new CommandLineReader();

        // this blocks the main Thread forever
        reader.ReadFromCommandLine();




        //ComponentLoader loader = new ComponentLoader();
        //loader.LoadJar("");
    }

}
