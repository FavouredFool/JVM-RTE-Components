package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class DeleteCommand implements ICommand {

    RTEManager _rteManager;
    Logger _logger;

    public DeleteCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {
        boolean success;
        try {
            success = _rteManager.get_componentManager().deleteComponent(Integer.parseInt(inputString));
        }
        catch(Exception e) {
            _logger.printMessageError("Enter an ID, not a name.");
            success = false;
        }

        if (success) {
            _logger.printMessageInfo("Component with ID " + inputString + " successfully deleted.");
            _rteManager.writeComponentsToJson(false);
        }
        else {
            _logger.printMessageInfo("Component with ID " + inputString + " could not be deleted.");
        }
    }
}
