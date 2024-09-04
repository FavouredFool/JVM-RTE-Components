package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class StopCommand implements Command{

    RTEManager _rteManager;
    Logger _logger;

    public StopCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {
        boolean success;
        try {
            success = _rteManager.get_componentManager().stopComponent(Integer.parseInt(inputString));
        }
        catch(Exception e) {
            _logger.printMessageError("Enter an ID, not a name.");
            success = false;
        }

        if (success) {
            _logger.printMessageInfo("Component with ID " + inputString + " successfully stopped.");
        }
        else {
            _logger.printMessageError("Component with ID " + inputString + " successfully stopped.");
        }
    }
}
