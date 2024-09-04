package org.components;

import org.logging.InjectAnnotation;
import org.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Component implements Runnable {

    ComponentState _componentState;
    int _id;
    String _path;
    ClassLoader _classLoader;
    Method _startMethod;
    Method _endMethod;
    Method _loadMethod;
    Class<?> _componentClass;
    Thread _thread;

    Logger _logger;

    public Component(int id, String path, ClassLoader classLoader, Method startMethod, Method endMethod, Method loadMethod, Class<?> componentClass){
        _componentState = ComponentState.SLEEP;
        _path = path;
        _id = id;
        _classLoader = classLoader;
        _startMethod = startMethod;
        _endMethod = endMethod;
        _loadMethod = loadMethod;
        _componentClass = componentClass;

        _logger = new Logger();
    }

    Object _startClassInstance;

    public void injectLogger(Object instance) {
        for (Field field : _componentClass.getDeclaredFields()){
            if (field.isAnnotationPresent(InjectAnnotation.class))
            {
                field.setAccessible(true);
                try {
                    field.set(instance, (Logger)new Logger());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        _logger.printMessageInfo("Injected Logger");
    }

    @Override
    public void run() {
        try {
            _startClassInstance = _componentClass.getDeclaredConstructor().newInstance();
            injectLogger(_startClassInstance);
            _startMethod.invoke(_startClassInstance, _id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            _endMethod.invoke(_startClassInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        _thread.interrupt();
    }

    public void processLoad(int stress) {
        try {
            _loadMethod.invoke(_startClassInstance, stress);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return "[Component(ID: " + _id + ", State: " + _componentState + ", Path: " + _path + ")]";
    }

    public ComponentState get_componentState() {
        return _componentState;
    }

    public int get_id() {
        return _id;
    }

    public String get_path() {
        return _path;
    }

    public ClassLoader get_classLoader() {
        return _classLoader;
    }

    public Method get_startMethod() {
        return _startMethod;
    }

    public Method get_endMethod() {
        return _endMethod;
    }

    public Method get_loadMethod() {
        return _loadMethod;
    }

    public Class<?> get_componentClass() {
        return _componentClass;
    }

    public Thread get_thread() {
        return _thread;
    }

}
