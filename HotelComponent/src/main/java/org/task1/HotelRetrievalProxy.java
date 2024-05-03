package org.task1;

import java.util.List;

public class HotelRetrievalProxy implements IHotelSearch {

    IHotelSearch _subject;

    public HotelRetrievalProxy(HotelRetrieval subject) {
        _subject = subject;
    }

    @Override
    public List<Hotel> getAllHotels() {
        return _subject.getAllHotels();
    }

    @Override
    public List<Hotel> getHotelsByName(String name) {
        return _subject.getHotelsByName(name);
    }

    @Override
    public List<Hotel> getHotelsByCity(String city) {
        return _subject.getHotelsByCity(city);
    }

    @Override
    public List<Hotel> getHotelsByIndex(int index) {
        return _subject.getHotelsByIndex(index);
    }

    @Override
    public void openSession() {
        _subject.openSession();
    }

    @Override
    public void closeSession() {
        _subject.closeSession();
    }
}
