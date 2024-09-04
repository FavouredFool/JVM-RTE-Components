package org.components.commands;

import org.components.RTEManager;

public class NullCommand implements Command{

    RTEManager _rteManager;

    public NullCommand(RTEManager rteManager) {
        _rteManager = rteManager;
    }

    @Override
    public void execute(String inputString) {

    }
}
