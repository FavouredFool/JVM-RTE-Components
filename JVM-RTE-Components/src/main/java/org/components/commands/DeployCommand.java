package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class DeployCommand implements Command{

    RTEManager _rteManager;
    Logger _logger;

    public DeployCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {
        if (_rteManager.get_componentManager().loadJar(inputString, -1, true)){
            _rteManager.writeComponentsToJson(false);
        }
        else {
            _logger.printMessageError(inputString + " could not be loaded.");
        }
    }
}
