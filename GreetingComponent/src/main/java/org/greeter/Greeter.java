package org.greeter;

import org.logging.Logger;

public class Greeter {

    static boolean _isStop = false;

    public static void InitialGreet(Logger myLog) {
        myLog.printMessageComponent("GreetingComponent [ID: " + Client._componentID + "]", "First Hello!");

        ContinuousGreet(myLog);
    }

    public static void ContinuousGreet(Logger myLog) {
        _isStop = false;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            myLog.printMessageComponent("GreetingComponent [ID: " + Client._componentID + "]", "Continuous Hello! [Number " + i + "]");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {

            }

            if (_isStop){
                myLog.printMessageComponent("GreetingComponent [ID: " + Client._componentID + "]", "Last Hello!");
                break;
            }

        }
    }

    public static void LastGreet() {
        _isStop = true;
    }
}
