package org.greeter;

import org.componentannotations.*;


@StartClassAnnotation
@StopClassAnnotation
public class Client {

    @StartMethodAnnotation
    public void startComponent() {
        Greeter.InitialGreet();
    }

    @StopMethodAnnotation
    public void endComponent() {
        Greeter.LastGreet();
    }
}
