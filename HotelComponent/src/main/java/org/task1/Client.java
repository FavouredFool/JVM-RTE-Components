package org.task1;

import org.componentannotations.*;

import java.util.List;

@StartClassAnnotation
@StopClassAnnotation
public class Client {

    @StartMethodAnnotation
    public void startComponent() {
        System.out.println("\n--- \"Hello, World!\" says the hotel-component.\n");

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

        List<Hotel> result = hotelSearchProxy.getHotelsByName("");

        System.out.println("Find all hotels:");

        for ( Hotel str : result ){
            System.out.println( str );
        }

        //cacheReferenceProxy.ClearCache();

        System.out.println("\nFind all hotels who's name contains the String \"org.task1.Hotel\":");

        List<Hotel> resultCached = hotelSearchProxy.getHotelsByName("org.task1.Hotel");
        for ( Hotel str : resultCached ){
            System.out.println( str );
        }

        hotelSearchProxy.closeSession();
    }

    @StopMethodAnnotation
    public void endComponent() {
        System.out.println("\n--- \"Goodbye, World.\" says the hotel-component.\n");
    }
}
