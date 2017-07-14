package com.vblynov.distribution.test.command;

import com.vblynov.distribution.client.Client;

public interface ClientCommand {

    void execute(Client client, String[] command);

    String getCommandName();
}
