package org.greeter;

public class Greeter {

    boolean _isStop = false;

    public void InitialGreet() {
        System.out.println("Greet Component: First Hello!");

        ContinuousGreet();
    }

    public void ContinuousGreet() {
        _isStop = false;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println("Greet Component: Continuous Hello Number " + i + "!");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (_isStop){
                System.out.println("Greet Component: Last Hello!");
                break;
            }

        }
    }

    public void LastGreet() {
        _isStop = true;
    }
}
