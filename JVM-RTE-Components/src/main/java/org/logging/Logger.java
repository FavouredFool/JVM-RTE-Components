package org.logging;

public class Logger {

    public void printMessage(String message) {
        // timestamp hinzufügen?
        System.out.println("++++ LOG: " + message);
    }

}
