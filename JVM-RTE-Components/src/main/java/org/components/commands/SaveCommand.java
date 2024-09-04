package org.components.commands;

import org.components.RTEManager;

public class SaveCommand implements Command{

    RTEManager _rteManager;

    public SaveCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {
        _rteManager.writeComponentsToJson(true);
        System.out.println("Saved configuration");
    }
}
