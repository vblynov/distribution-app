package com.vblynov.distribution.client.response;

public interface ClientFutureListener<T extends ClientFuture<?>> {

    void operationComplete(T future);

}
