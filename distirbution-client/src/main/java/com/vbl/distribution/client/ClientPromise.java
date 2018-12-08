package com.vbl.distribution.client;

import com.vbl.distribution.client.response.ClientFuture;

/**
 * Extension of {@link ClientFuture} interface which provides methods for future completion
 *
 * @param <V>
 */
interface ClientPromise<V> extends ClientFuture<V> {

    /**
     * Resolve the future with success
     *
     * @param result future result instance
     */
    void setSuccess(V result);

    /**
     * Resolve future with failure
     * @param cause exception which caused failure
     */
    void setFailure(Throwable cause);

}
