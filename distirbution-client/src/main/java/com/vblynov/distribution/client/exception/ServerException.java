package com.vblynov.distribution.client.exception;

import com.vblynov.distirbution.model.Error;

public class ServerException extends RuntimeException {
    private final Error serverError;


    public ServerException(Error serverError) {
        this.serverError = serverError;
    }

    public Error getServerError() {
        return serverError;
    }
}
