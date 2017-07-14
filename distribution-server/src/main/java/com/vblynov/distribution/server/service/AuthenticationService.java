package com.vblynov.distribution.server.service;

import com.vblynov.distirbution.model.LoginRequest;
import com.vblynov.distribution.server.service.exception.ServerException;

public class AuthenticationService {

    public String authenticateConsumer(LoginRequest loginRequest) {
        if (loginRequest.getUser().equals(loginRequest.getPassword())) {
            return loginRequest.getUser();
        }
        throw new ServerException("User not found");
    }


}
