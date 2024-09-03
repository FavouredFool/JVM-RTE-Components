package org.greeter;

import org.componentannotations.*;
import org.logging.*;

public class Client {

    static int _componentID;

    @InjectAnnotation
    private Logger myLog;

    @StartMethodAnnotation
    public void startComponent(int componentID) {
        _componentID = componentID;

        if (myLog != null){
            myLog.printMessage("Meldung aus GreetingComponent [ID: " + Client._componentID + "]: Prozess gestartet");
        }

        Greeter.InitialGreet();
    }

    @StopMethodAnnotation
    public void endComponent() {
        if (myLog != null) {
            myLog.printMessage("Meldung aus GreetingComponent [ID: " + Client._componentID + "]: Prozess beendet");
        }

        Greeter.LastGreet();
    }
}
