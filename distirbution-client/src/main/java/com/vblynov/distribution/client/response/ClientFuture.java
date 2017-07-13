package com.vblynov.distribution.client.response;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface ClientFuture<V> extends Future<V> {

    ClientFuture<V> listener(ClientFutureListener<? super ClientFuture<V>> listener);

    void await() throws InterruptedException;

    boolean await(long time, TimeUnit unit) throws InterruptedException, TimeoutException;

    boolean isSuccess();

}
