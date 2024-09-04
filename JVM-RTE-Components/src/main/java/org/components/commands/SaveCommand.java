package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class SaveCommand implements Command{

    RTEManager _rteManager;
    Logger _logger;

    public SaveCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {
        _rteManager.writeComponentsToJson(true);
        _logger.printMessageInfo("Saved configuration");
    }
}
