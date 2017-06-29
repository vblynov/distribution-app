package com.vblynov.distribution.server.service;

import com.vblynov.distirbution.model.LoginRequest;
import com.vblynov.distribution.server.service.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

public class AuthenticationService {

    public String authenticateConsumer(LoginRequest loginRequest) throws UnauthorizedException {
        if (loginRequest.getUser().equals(loginRequest.getPassword())) {
            return loginRequest.getUser();
        }
        throw new UnauthorizedException("User not found");
    }


}
