package com.vblynov.distribution.client;

import com.vblynov.distribution.client.response.ClientFuture;

interface ClientPromise<V> extends ClientFuture<V> {

    void setSuccess(V result);

    void setFailure(Throwable cause);

}
