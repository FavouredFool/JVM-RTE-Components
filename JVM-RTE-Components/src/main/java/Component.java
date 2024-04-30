import java.lang.reflect.Method;
import java.util.UUID;

public class Component {

    ComponentState _componentState;
    int _id;
    String _name;
    ClassLoader _classLoader;
    Method _startMethod;
    Method _endMethod;



    public Component(int id, String name, ClassLoader classLoader, Method startMethod, Method endMethod){
        _componentState = ComponentState.SLEEP;
        _name = name;
        _id = id;
        _classLoader = classLoader;
        _startMethod = startMethod;
        _endMethod = endMethod;
    }


    public String GetStatus() {
        return "[Component(ID: " + _id + ", Name: " + _name + ", State: " + _componentState + ")]";
    }
}
