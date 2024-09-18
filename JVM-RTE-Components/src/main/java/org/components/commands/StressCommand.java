package org.components.commands;

import org.components.RTEManager;
import org.logging.Logger;

public class StressCommand implements ICommand {

    RTEManager _rteManager;
    Logger _logger;

    public StressCommand(RTEManager rteManager) {
        _rteManager = rteManager;
        _logger = new Logger();
    }

    @Override
    public void execute(String inputString) {
        int stress;
        try {
            stress = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            _logger.printMessageError("Did not provide a length of stress (integer");
            return;
        }

        _rteManager.get_componentManager().stressComponent(stress);
    }
}
