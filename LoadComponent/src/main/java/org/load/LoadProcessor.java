package org.load;

import org.logging.Logger;

import java.util.LinkedList;
import java.util.Queue;

public class LoadProcessor {

    static boolean _isInterrupted;

    static Queue<Integer> _processingQueue;

    public static void queueProcessing(Logger myLog){
        int sizeOfLoad = 5;
        int positionInQueue = _processingQueue.size();

        _processingQueue.offer(sizeOfLoad);

        myLog.printMessage("Meldung aus LoadComponent: Load " + sizeOfLoad + " wurde gequeued in Position " + positionInQueue +".");
    }

    public static void process(Logger myLog) {
        while (!_isInterrupted) {
            if (_processingQueue.isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {

                }
                continue;
            }

            int currentLoadSize = _processingQueue.peek();
            myLog.printMessage("Meldung aus LoadComponent: Processing von Load Size " + currentLoadSize + " wurde gestartet.");

            // This simulates load
            try {
                Thread.sleep(currentLoadSize * 1000L);
            } catch (InterruptedException ignored) {

            }

            // Remove the processed item
            _processingQueue.poll();

            if (_isInterrupted){
                // Empty the queue, because the component is being turned off
                _processingQueue.clear();
                myLog.printMessage("Meldung aus LoadComponent: Verarbeitung wurde abgebrochen");
                break;
            }

            myLog.printMessage("Meldung aus LoadComponent: Verarbeitung von Load Size " + currentLoadSize + " wurde abgeschlossen");

            if (_processingQueue.isEmpty()) {
                myLog.printMessage("Meldung aus LoadComponent: Queue ist leer");
            }
        }
    }


    public static void prepareProcessing() {
        _processingQueue = new LinkedList<>();
        _isInterrupted = false;
    }

    public static void interruptProcessing() {
        _isInterrupted = true;
    }
}
