package org.load;

public class LoadProcessor {

    static boolean _isProcessing;
    static boolean _isInterrupted;

    public static boolean loadProcessing() {
        _isProcessing = true;

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ignored) {

        }

        _isProcessing = false;

        if (_isInterrupted){
            return false;
        }

        return true;
    }

    public static void prepareProcessing() {
        _isProcessing = false;
        _isInterrupted = false;
    }

    public static void interruptProcessing() {
        _isInterrupted = true;
    }
}
