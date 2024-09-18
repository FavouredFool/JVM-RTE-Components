package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class NullCommand implements ICommand {

    RTEManager _rteManager;
    Logger _logger;

    public NullCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {

    }
}
