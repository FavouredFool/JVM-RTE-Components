package org.components;

import java.lang.reflect.Method;

public class Component {

    ComponentState _componentState;
    int _id;
    String _path;
    ClassLoader _classLoader;
    Method _startMethod;
    Method _endMethod;



    public Component(int id, String path, ClassLoader classLoader, Method startMethod, Method endMethod){
        _componentState = ComponentState.SLEEP;
        _path = path;
        _id = id;
        _classLoader = classLoader;
        _startMethod = startMethod;
        _endMethod = endMethod;
    }


    public String toString() {
        return "[Component(ID: " + _id + ", State: " + _componentState + ", Path: " + _path + ")]";
    }
}
