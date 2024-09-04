package org.load;

import org.componentannotations.*;
import org.logging.*;

public class Client {

    static int _componentID;

    @InjectAnnotation
    private Logger myLog;

    @StartMethodAnnotation
    public void startComponent(int componentID) {

        _componentID = componentID;

        if (myLog == null) {
            System.out.println("ERROR: Logger not injected!");
            return;
        }

        myLog.printMessageComponent("LoadComponent [ID: " + Client._componentID + "]", "Prozess gestartet");

        LoadProcessor.prepareProcessing();
        LoadProcessor.process(myLog);
    }

    @StopMethodAnnotation
    public void endComponent() {

        if (myLog == null) {
            System.out.println("ERROR: Logger not injected!");
            return;
        }

        myLog.printMessageComponent("LoadComponent [ID: " + Client._componentID + "]", "Prozess beendet");

        LoadProcessor.interruptProcessing();
    }

    @LoadMethodAnnotation
    public void applyLoad(int stress) {
        LoadProcessor.queueProcessing(myLog, stress);
    }
}
