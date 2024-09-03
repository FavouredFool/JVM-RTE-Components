package org.load;

import org.componentannotations.*;
import org.logging.*;

public class Client {

    @InjectAnnotation
    private Logger myLog;

    @StartMethodAnnotation
    public void startComponent() {
        if (myLog != null){
            myLog.printMessage("Meldung aus LoadComponent: Prozess gestartet");
        }

        LoadProcessor.prepareProcessing();
        LoadProcessor.process(myLog);
    }

    @StopMethodAnnotation
    public void endComponent() {
        if (myLog != null) {
            myLog.printMessage("Meldung aus LoadComponent: Prozess beendet");
        }

        LoadProcessor.interruptProcessing();
    }

    @LoadMethodAnnotation
    public void applyLoad() {
        LoadProcessor.queueProcessing(myLog);
    }
}
