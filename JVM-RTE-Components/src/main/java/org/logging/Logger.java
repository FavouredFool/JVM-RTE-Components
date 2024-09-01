package org.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public void printMessage(String message) {
        // timestamp hinzufügen?


        //From: https://stackoverflow.com/questions/23068676/how-to-get-current-timestamp-in-string-format-in-java-yyyy-mm-dd-hh-mm-ss
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time
        String formattedNow = now.format(formatter);

        System.out.println("++++ LOG: " + message + " [" + formattedNow + "]");
    }

}
