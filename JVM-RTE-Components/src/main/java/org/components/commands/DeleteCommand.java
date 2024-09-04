package org.components.commands;

import org.components.RTEManager;

public class DeleteCommand implements Command{

    RTEManager _rteManager;

    public DeleteCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {
        boolean success;
        try {
            success = _rteManager.get_componentManager().deleteComponent(Integer.parseInt(inputString));
        }
        catch(Exception e) {
            System.out.println("error: Enter an ID, not a name.");
            success = false;
        }

        if (success) {
            System.out.println("Component with ID " + inputString + " successfully deleted.");
            _rteManager.writeComponentsToJson(false);
        }
        else {
            System.out.println("Component with ID " + inputString + " could not be deleted.");
        }
    }
}
