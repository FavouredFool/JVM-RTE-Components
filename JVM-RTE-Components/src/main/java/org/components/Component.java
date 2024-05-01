package org.components;

import java.lang.reflect.Method;

public class Component {

    ComponentState _componentState;
    int _id;
    String _path;
    ClassLoader _classLoader;
    Method _startMethod;
    Method _endMethod;
    Class<?> _startClass;
    Class<?> _endClass;



    public Component(int id, String path, ClassLoader classLoader, Method startMethod, Method endMethod, Class<?> startClass, Class<?> endClass){
        _componentState = ComponentState.SLEEP;
        _path = path;
        _id = id;
        _classLoader = classLoader;
        _startMethod = startMethod;
        _endMethod = endMethod;
        _startClass = startClass;
        _endClass = endClass;
    }


    public String toString() {
        return "[Component(ID: " + _id + ", State: " + _componentState + ", Path: " + _path + ")]";
    }
}
