package org.task1;

import org.componentannotations.*;

import java.util.List;

@StartClassAnnotation
@StopClassAnnotation
public class Client {

    @StartMethodAnnotation
    public void startComponent() {
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

        RepeatingRetrieval.InitialRetrieval(hotelSearchProxy);

        hotelSearchProxy.closeSession();
    }

    @StopMethodAnnotation
    public void endComponent() {
        RepeatingRetrieval.StopRetrieval();
    }
}
