package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class StatusCommand implements ICommand {

    RTEManager _rteManager;
    Logger _logger;

    public StatusCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {

        if (inputString.isEmpty()){
            _logger.printMessageInfo("Status for all Components:");
        }
        else {
            _logger.printMessageInfo("Status for Component with ID " + inputString + ":");
        }

        System.out.println(_rteManager.get_componentManager().getComponentStatus(inputString));
    }
}
