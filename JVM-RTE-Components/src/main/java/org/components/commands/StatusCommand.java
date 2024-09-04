package org.components.commands;

import org.components.RTEManager;

public class StatusCommand implements Command{

    RTEManager _rteManager;

    public StatusCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {

        if (inputString.isEmpty()){
            System.out.println("Status for all Components:");
        }
        else {
            System.out.println("Status for Component with ID " + inputString + ":");
        }

        System.out.println(_rteManager.get_componentManager().getComponentStatus(inputString));
    }
}
