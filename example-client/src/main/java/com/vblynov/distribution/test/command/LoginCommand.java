package com.vblynov.distribution.test.command;

import com.vblynov.distirbution.model.LoginRequest;
import com.vblynov.distirbution.model.LoginResponse;
import com.vblynov.distribution.client.Client;
import com.vblynov.distribution.client.response.ClientFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginCommand implements ClientCommand {
    private static final Logger LOG  = LoggerFactory.getLogger(LoginCommand.class);

    @Override
    public void execute(Client client, String[] command) {
        ClientFuture<LoginResponse> response = client.login(LoginRequest.newBuilder()
                .setUser(command[2])
                .setPassword(command[3])
                .build());
        try {
            LoginResponse loginResponse = response.get(10000, TimeUnit.MILLISECONDS);
            LOG.info("Got login response {}", loginResponse.getResult());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.error("Exception thrown ", e);
        }

    }

    @Override
    public String getCommandName() {
        return "login";
    }
}
