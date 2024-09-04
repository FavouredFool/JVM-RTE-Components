package org.task1;

import org.componentannotations.*;
import org.logging.*;
import java.util.List;

public class Client {

    static int _componentID;

    @InjectAnnotation
    private Logger myLog;

    @StartMethodAnnotation
    public void startComponent(int componentID) {

        _componentID = componentID;

        if (myLog == null) {
            System.out.println("ERROR: Logger not injected!");
            return;
        }

        myLog.printMessageComponent("HotelComponent [ID: " + Client._componentID + "]", "Prozess gestartet");

        // Creates hotelRetrieval.
        HotelRetrieval hotelRetrieval = new HotelRetrieval();

        // Gets ports.
        IHotelSearch hotelSearchProxy = hotelRetrieval.getHotelSearchProxy();
        ICacheReference cacheReferenceProxy = hotelRetrieval.getCacheReferenceProxy();

        // Creates new org.task1.Cache
        Cache cache = new Cache();

        // Injects org.task1.ICaching-Proxy into org.task1.HotelRetrieval through port.
        cacheReferenceProxy.SetCacheReference(cache.getCacheProxy());

        // Simulates how an extern client (outside of the component) would use hotelSearch.
        hotelSearchProxy.openSession();

        RepeatingRetrieval.InitialRetrieval(hotelSearchProxy, myLog);

        hotelSearchProxy.closeSession();
    }

    @StopMethodAnnotation
    public void endComponent() {

        if (myLog == null) {
            System.out.println("ERROR: Logger not injected!");
            return;
        }

        myLog.printMessageComponent("HotelComponent [ID: " + Client._componentID + "]", "Prozess beendet");

        RepeatingRetrieval.StopRetrieval();
    }
}
