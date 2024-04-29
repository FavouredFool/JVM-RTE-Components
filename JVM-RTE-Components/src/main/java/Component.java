import java.util.UUID;

public class Component {

    ComponentState _componentState;
    String _name;
    ClassLoader _classLoader;


    public Component(String name, ClassLoader classLoader){
        _componentState = ComponentState.SLEEP;
        _name = name;
        _classLoader = classLoader;
    }


    public String GetStatus() {
        return "[Component(Name: " + _name + ") is in State " + _componentState + "]";
    }
}
