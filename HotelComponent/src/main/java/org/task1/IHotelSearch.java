package org.task1;

import java.util.List;

public interface IHotelSearch {
    List<Hotel> getAllHotels();
    List<Hotel> getHotelsByName(String name);
    List<Hotel> getHotelsByCity(String city);
    List<Hotel> getHotelsByIndex(int index);

    void openSession();
    void closeSession();
}
