package org.greeter;

import org.componentannotations.*;


@StartClassAnnotation
@StopClassAnnotation
public class Client {

    @StartMethodAnnotation
    public void startComponent() {
        System.out.println("\n--- \"Hello, World!\" says the greet-component.\n");

        Greeter greeter = new Greeter();
        greeter.InitialGreet();
    }

    @StopMethodAnnotation
    public void endComponent() {
        System.out.println("\n--- \"Goodbye, World.\" says the greet-component.\n");
    }
}
