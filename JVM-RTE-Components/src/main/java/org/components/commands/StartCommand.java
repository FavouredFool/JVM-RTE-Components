package org.components.commands;

import org.components.RTEManager;

public class StartCommand implements Command{

    RTEManager _rteManager;

    public StartCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {
        boolean success;
        try {
            success = _rteManager.get_componentManager().startComponent(Integer.parseInt(inputString));
        }
        catch(Exception e) {
            System.out.println("error: Enter an ID, not a name.");
            success = false;
        }

        if (success) {
            System.out.println("Component with ID " + inputString + " successfully started.");
        }
        else {
            System.out.println("Component with ID " + inputString + " could not be started.");
        }
    }
}
