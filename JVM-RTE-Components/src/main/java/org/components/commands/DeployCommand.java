package org.components.commands;

import org.components.RTEManager;

public class DeployCommand implements Command{

    RTEManager _rteManager;

    public DeployCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {
        if (_rteManager.get_componentManager().loadJar(inputString, -1, true)){
            _rteManager.writeJson(false);
        }
        else {
            System.out.println(inputString + " could not be loaded.");
        }
    }
}
