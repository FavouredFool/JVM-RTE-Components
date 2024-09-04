package org.components.commands;

import org.components.RTEManager;

public class StressCommand implements Command{

    RTEManager _rteManager;

    public StressCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {
        int stress;
        try {
            stress = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            System.out.println("error: did not provide a length of stress (integer)");
            return;
        }

        _rteManager.get_componentManager().stressComponent(stress);
    }
}
