package com.vbl.distribution.test.command;

import com.vbl.distribution.client.Client;

public interface ClientCommand {

    void execute(Client client, String[] command);

    String getCommandName();
}
