package com.vbl.distribution.test.command;

import com.vbl.distirbution.model.*;
import com.vbl.distribution.client.Client;
import com.vbl.distribution.client.response.ClientFuture;
import com.vbl.distribution.client.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OptionRequestCommand implements ClientCommand {
    private static final Logger LOG = LoggerFactory.getLogger(OptionRequestCommand.class);

    @Override
    public void execute(Client client, String[] command) {
        OptionRequest.Builder optionRequest = OptionRequest.newBuilder();
        if (command.length > 2) {
            String identifiersConfig = command[2];
            String[] identifiers = identifiersConfig.substring(1, identifiersConfig.length() - 1).split(",");
            for (String identifier : identifiers) {
                int colonPosition = identifier.indexOf(':');
                OptionIdentifierType type = OptionIdentifierType.valueOf(identifier.substring(0, colonPosition));
                String value = identifier.substring(colonPosition + 1);
                optionRequest.addIdentifiers(OptionIdentifier.newBuilder().setType(type).setValue(value).build());
            }
        }
        ClientFuture<Response> response = client.get(optionRequest.build(), opt -> LOG.info("got option {}", opt));
        try {
            response.get(10000, TimeUnit.MILLISECONDS);
            LOG.info("DONE");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.error("Exception thrown ", e);
        }
    }

    @Override
    public String getCommandName() {
        return "option";
    }
}
