package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class StartCommand implements Command{

    RTEManager _rteManager;
    Logger _logger;

    public StartCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {
        boolean success;
        try {
            success = _rteManager.get_componentManager().startComponent(Integer.parseInt(inputString));
        }
        catch(Exception e) {
            _logger.printMessageError("Enter an ID, not a name.");
            success = false;
        }

        if (success) {
            _logger.printMessageInfo("Component with ID " + inputString + " successfully started.");
        }
        else {
            _logger.printMessageError("Component with ID " + inputString + " could not be started.");
        }
    }
}
