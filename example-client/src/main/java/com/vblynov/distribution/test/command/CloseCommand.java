package com.vblynov.distribution.test.command;

import com.vblynov.distribution.client.Client;

public class CloseCommand implements ClientCommand {

    @Override
    public void execute(Client client, String[] command) {
        client.close();
    }

    @Override
    public String getCommandName() {
        return "close";
    }
}
