package org.greeter;

public class Greeter {

    static boolean _isStop = false;

    public static void InitialGreet() {
        System.out.println("Greeting Component: First Hello!");

        ContinuousGreet();
    }

    public static void ContinuousGreet() {
        _isStop = false;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println("Greeting Component: Continuous Hello! [Number " + i + "]");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {

            }

            if (_isStop){
                System.out.println("Greeting Component: Last Hello!");
                break;
            }

        }
    }

    public static void LastGreet() {
        _isStop = true;
    }
}
