package org.greeter;

import org.componentannotations.*;


@StartClassAnnotation
@StopClassAnnotation
public class Client {

    Greeter _greeter;

    @StartMethodAnnotation
    public void startComponent() {
        System.out.println("\n--- \"Hello, World!\" says the greet-component.\n");

        _greeter = new Greeter();
        _greeter.InitialGreet();
    }

    @StopMethodAnnotation
    public void endComponent() {
        _greeter.LastGreet();
        System.out.println("\n--- \"Goodbye, World.\" says the greet-component.\n");
    }
}
