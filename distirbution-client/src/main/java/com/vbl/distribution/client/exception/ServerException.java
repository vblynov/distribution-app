package com.vbl.distribution.client.exception;

import com.vbl.distirbution.model.Error;

/**
 * Client-side runtime exception to wrap {@link Error} message from server
 */
public class ServerException extends RuntimeException {
    private final Error serverError;


    public ServerException(Error serverError) {
        this.serverError = serverError;
    }

    public Error getServerError() {
        return serverError;
    }

    @Override
    public String getMessage() {
        return "Received server error " + serverError;
    }
}
