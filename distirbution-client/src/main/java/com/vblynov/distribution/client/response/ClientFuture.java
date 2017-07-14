package com.vblynov.distribution.client.response;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Client-side future aimed to provide more clean and flexible asynchronous API.
 *
 * @param <V> type of response
 */
public interface ClientFuture<V> extends Future<V> {

    /**
     * Set listener which wil lbe triggered upon future completion. There coud be no more than one listener per future instance
     *
     * @param listener listener instance
     * @return future instance to enable method chaining
     */
    ClientFuture<V> listener(ClientFutureListener<? super ClientFuture<V>> listener);

    /**
     * Wait for future to complete
     * @throws InterruptedException if waiting thread is interrupted
     */
    void await() throws InterruptedException;

    /**
     * Wait for the competion for specified amount of time
     *
     * @param time number of time units to wait
     * @param unit time unit definition
     * @return true, if the future is completed before timeout occurs, false otherwise
     * @throws InterruptedException in case waiting thread is interruped
     */
    boolean await(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Checks the future completion status
     * @return true if future completed successfully, fasle otherwise
     */
    boolean isSuccess();

}
