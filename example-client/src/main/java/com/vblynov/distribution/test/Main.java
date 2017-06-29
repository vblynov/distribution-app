package com.vblynov.distribution.test;

import com.vblynov.distribution.client.Client;
import com.vblynov.distribution.client.DistributionClientFactory;
import com.vblynov.distribution.test.command.ClientCommand;
import com.vblynov.distribution.test.command.CloseCommand;
import com.vblynov.distribution.test.command.LoginCommand;
import com.vblynov.distribution.test.command.OptionRequestCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static Map<String, ClientCommand> COMMAND_MAP = new HashMap<>();

    static {
        ClientCommand command = new CloseCommand();
        COMMAND_MAP.put(command.getCommandName(), command);
        command = new LoginCommand();
        COMMAND_MAP.put(command.getCommandName(), command);
        command = new OptionRequestCommand();
        COMMAND_MAP.put(command.getCommandName(), command);
    }

    public static void main(String args[]) throws InterruptedException {
        Map<String, Client> clientMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        boolean finished = false;
        while (!finished) {
            try {
                String command = scanner.nextLine();
                String[] splittedCommand = command.split(" ");
                if ("exit".equals(splittedCommand[0])) {
                    finished = true;
                } else if ("new".equals(splittedCommand[0])) {
                    Client client = DistributionClientFactory.createClient("localhost", 7777);
                    clientMap.put(splittedCommand[1], client);
                } else {
                    String clientName = splittedCommand[0];
                    Client client = clientMap.get(clientName);
                    if (client == null) {
                        LOG.info("Client not found");
                        continue;
                    }
                    ClientCommand clientCommand = COMMAND_MAP.get(splittedCommand[1]);
                    clientCommand.execute(client, splittedCommand);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
