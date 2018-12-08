package com.vbl.distribution.test.command;

import com.vbl.distribution.client.Client;

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
