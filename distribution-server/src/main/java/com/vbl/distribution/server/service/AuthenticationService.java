package com.vbl.distribution.server.service;

import com.vbl.distribution.server.service.exception.ServerException;
import com.vbl.distirbution.model.LoginRequest;

public class AuthenticationService {

    public String authenticateConsumer(LoginRequest loginRequest) {
        if (loginRequest.getUser().equals(loginRequest.getPassword())) {
            return loginRequest.getUser();
        }
        throw new ServerException("User not found");
    }


}
