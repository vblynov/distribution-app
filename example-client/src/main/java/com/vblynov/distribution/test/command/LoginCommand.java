package com.vblynov.distribution.test.command;

import com.vblynov.distirbution.model.Error;
import com.vblynov.distirbution.model.LoginRequest;
import com.vblynov.distirbution.model.LoginResponse;
import com.vblynov.distribution.client.Client;
import com.vblynov.distribution.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginCommand implements ClientCommand {
    private static final Logger LOG  = LoggerFactory.getLogger(LoginCommand.class);

    @Override
    public void execute(Client client, String[] command) {
        client.login(LoginRequest.newBuilder()
                .setUser(command[2])
                .setPassword(command[3])
                .build(), LOGIN_HANDLER);

    }

    @Override
    public String getCommandName() {
        return "login";
    }

    private static final ResponseHandler<LoginResponse> LOGIN_HANDLER = new ResponseHandler<LoginResponse>() {

        @Override
        public void onResponse(LoginResponse response) {
            LOG.info("Got login response " + response.getResult());
        }

        @Override
        public void onError(Error error) {
            LOG.info("ERROR " + error);
        }
    };
}
