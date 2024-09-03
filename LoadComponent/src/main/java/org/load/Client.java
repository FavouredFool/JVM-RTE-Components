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

        myLog.printMessage("Meldung aus LoadComponent: Load wurde gequeued [Position " + "X" +"]");

        if (LoadProcessor._isProcessing) {
            myLog.printMessage("Meldung aus LoadComponent: Load kann nicht verarbeitet werden [busy]");
        }
        else {
            if (LoadProcessor.loadProcessing()){
                myLog.printMessage("Meldung aus LoadComponent: Verarbeitung wurde abgeschlossen [success]");
            }
            else {
                myLog.printMessage("Meldung aus LoadComponent: Verarbeitung wurde abgebrochen [stopped]");
            }
        }



    }
}
