package org.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public void printMessage(String message) {
        //From: https://stackoverflow.com/questions/23068676/how-to-get-current-timestamp-in-string-format-in-java-yyyy-mm-dd-hh-mm-ss

        System.out.println("++++ LOG: " + message + " " + getTimestamp());
    }

    public void printMessageComponent(String component, String message) {
        System.out.println("++++ COMPONENT " + component + ": " + message + " " + getTimestamp());
    }

    public void printMessageInfo(String message) {
        System.out.println("++++ INFO: " + message + " " + getTimestamp());
    }

    public void printMessageError(String message) {
        System.out.println("++++ ERROR: " + message + " " + getTimestamp());
    }

    public void printEmpty(){
        System.out.println("");
    }

    public String getTimestamp() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time
        String formattedNow = now.format(formatter);

        return "[" + formattedNow + "]";
    }

}
