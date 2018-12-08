package com.vbl.distribution.client.response;

/**
 * Callback interface which is to be notified if the future is completed
 *
 * @param <T> corresponding future type
 */
public interface ClientFutureListener<T extends ClientFuture<?>> {

    /**
     * Callback method to process completed future
     *
     * @param future completed future instance
     */
    void operationComplete(T future);

}
