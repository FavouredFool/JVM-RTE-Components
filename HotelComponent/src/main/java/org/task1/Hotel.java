package org.task1;

public class Hotel {

    int _index;
    String _name;
    String _city;

    public Hotel(int index, String name, String city) {
        _index = index;
        _name = name;
        _city = city;
    }

    @Override
    public String toString() {
        return "[Hotel(index: " + _index + ", name: " + _name + ", city: " + _city + ")]";
    }
}
