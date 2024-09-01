package org.task1;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RepeatingRetrieval {

    static boolean _isStop = false;

    public static String[] _queries = {"Berg", "Hof", "Maier", "Bay", "Winzer", "Plaza"};


    public static void InitialRetrieval(IHotelSearch hotelSearchProxy) {
        System.out.println("Hotel Component: Get all Hotels");

        List<Hotel> result = hotelSearchProxy.getHotelsByName("");

        for ( Hotel str : result ){
            System.out.println( str );
        }

        ContinuousRetrieval(hotelSearchProxy);
    }

    public static void ContinuousRetrieval(IHotelSearch hotelSearchProxy) {
        _isStop = false;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String query = _queries[ThreadLocalRandom.current().nextInt(0, _queries.length)];

            System.out.println("Hotel Component: Continuous Retrieval by Name [Name: '" + query + "']");

            //System.out.println("\nFind all hotels who's name contains the String \"Hotel\":");

            List<Hotel> resultCached = hotelSearchProxy.getHotelsByName(query);
            for ( Hotel str : resultCached ){
                System.out.println( str );
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {

            }

            if (_isStop){
                break;
            }

        }
    }

    public static void StopRetrieval() {
        _isStop = true;
    }
}
