package org.greeter;

import org.componentannotations.*;
import org.logging.*;

public class Client {

    @InjectAnnotation
    private Logger myLog;

    @StartMethodAnnotation
    public void startComponent() {
        if (myLog != null){
            myLog.printMessage("Meldung aus GreetingComponent: Prozess gestartet");
        }

        Greeter.InitialGreet();
    }

    @StopMethodAnnotation
    public void endComponent() {
        if (myLog != null) {
            myLog.printMessage("Meldung aus GreetingComponent: Prozess beendet");
        }

        Greeter.LastGreet();
    }
}
