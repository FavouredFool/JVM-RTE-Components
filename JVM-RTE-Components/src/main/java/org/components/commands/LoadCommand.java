package org.components.commands;

import org.components.ComponentManager;
import org.components.RTEManager;

public class LoadCommand implements Command{

    RTEManager _rteManager;

    public LoadCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {
        _rteManager.get_componentManager().load(inputString);
    }
}
