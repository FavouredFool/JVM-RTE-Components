package org.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Component implements Runnable {

    ComponentState _componentState;
    int _id;
    String _path;
    ClassLoader _classLoader;
    Method _startMethod;
    Method _endMethod;
    Class<?> _startClass;
    Class<?> _endClass;
    Thread _thread;



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

    @Override
    public void run() {
        try {
            Object test = _startClass.getDeclaredConstructor().newInstance();
            _startMethod.invoke(test);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {

        try {
            Object test = _endClass.getDeclaredConstructor().newInstance();
            _endMethod.invoke(test);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        _thread.interrupt();
    }
}
