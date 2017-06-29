package com.vblynov.distribution.test.command;

import com.vblynov.distirbution.model.*;
import com.vblynov.distirbution.model.Error;
import com.vblynov.distribution.client.Client;
import com.vblynov.distribution.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                String value = identifier.substring(colonPosition + 1, identifier.length());
                optionRequest.addIdentifiers(OptionIdentifier.newBuilder().setType(type).setValue(value).build());
            }
        }
        optionRequest.setSample(Option.newBuilder().setDescription("desc").build());
        client.get(optionRequest.build(), OPTION_HANDLER);
    }

    @Override
    public String getCommandName() {
        return "option";
    }

    private static final ResponseHandler<Option> OPTION_HANDLER = new ResponseHandler<Option>() {
        @Override
        public void onResponse(Option response) {
            LOG.info("Got option " + response);
        }

        @Override
        public void onError(Error error) {
            LOG.info("Option request ERROR " + error);
        }
    };
}
