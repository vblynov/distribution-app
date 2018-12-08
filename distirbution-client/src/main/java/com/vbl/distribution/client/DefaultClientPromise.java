package com.vbl.distribution.client;

import com.vbl.distribution.client.response.ClientFuture;
import com.vbl.distribution.client.response.ClientFutureListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Default {@link ClientPromise} implementation. The implementation makes use of some ideas from similar purpose classes
 * from  {@link io.netty.util.concurrent} package.
 *
 * @param <V> result type
 */
class DefaultClientPromise<V> implements ClientPromise<V> {

    private Future<?> wrappedFuture = null;
    private AtomicReference<Object> result = new AtomicReference<>();
    private volatile ClientFutureListener<? super ClientFuture<V>> listener;
    private short waitersCount = 0;

    DefaultClientPromise() {

    }

    DefaultClientPromise(Future<?> wrappedFuture) {
        this.wrappedFuture = wrappedFuture;
    }


    @Override
    public void setSuccess(V result) {
        if (setValue(result)) {
            notifyListener();
        }
    }

    @Override
    public void setFailure(Throwable cause) {
        if (setValue(cause)) {
            notifyListener();
        }
    }

    @Override
    public ClientFuture<V> listener(ClientFutureListener<? super ClientFuture<V>> listener) {
        if (listener != null) {
            synchronized (this) {
                if (this.listener != null) {
                    throw new IllegalArgumentException("Only one listener is supported");
                } else {
                    this.listener = listener;
                }
            }
        }
        return this;
    }

    @Override
    public void await() throws InterruptedException {
        if (isDone()) {
            return;
        }
        try {
            waitWrappedFuture();
        } catch (ExecutionException e) {
            setFailure(e);
            return;
        }
        checkInterrupted();
        synchronized (this) {
            while (!isDone()) {
                waitersCount++;
                try {
                    wait();
                } finally {
                    waitersCount--;
                }
            }
        }
    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException {
        if (isDone()) {
            return true;
        }
        checkInterrupted();
        long remainingTime;
        try {
            remainingTime = waitWrappedFuture(time, unit);
        } catch (ExecutionException e) {
            setFailure(e);
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
        if (remainingTime == 0) {
            return false;
        }
        synchronized (this) {
            if (isDone()) {
                return true;
            }
            waitersCount++;
            try {
                wait(remainingTime / 1000000, (int) (remainingTime % 1000000));
            } finally {
                waitersCount--;
            }
        }
        return isDone();
    }

    @Override
    public boolean isFailed() {
        return !isDone() || result.get() instanceof Throwable;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("cancel is unsupported");
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return result.get() != null;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        await();
        Object operationResult = result.get();
        if (isFailed()) {
            throw new ExecutionException((Throwable) operationResult);
        }
        return (V) operationResult;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (await(timeout, unit)) {
            Object operationResult = result.get();
            if (isFailed()) {
                throw new ExecutionException((Throwable) operationResult);
            }
            return (V) operationResult;
        }
        throw new TimeoutException("Request timed-out");
    }

    private void notifyListener() {
        synchronized (this) {
            if (listener != null) {
                listener.operationComplete(this);
            }
        }
    }

    private void waitWrappedFuture() throws ExecutionException, InterruptedException {
        if (wrappedFuture != null) {
            wrappedFuture.get();
        }
    }

    private long waitWrappedFuture(long time, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        if (wrappedFuture != null) {
            long startTime = System.nanoTime();
            wrappedFuture.get(time, timeUnit);
            long elapsedWaitingTime = time - (System.nanoTime() - startTime);
            long remainingTime = timeUnit.toNanos(time) - elapsedWaitingTime;
            if (remainingTime < 0) {
                remainingTime = 0;
            }
            return remainingTime;
        }
        return timeUnit.toNanos(time);
    }

    private boolean setValue(Object value) {
        if (result.compareAndSet(null, value)) {
            synchronized (this) {
                if (waitersCount > 0) {
                    notifyAll();
                }
            }
            return true;
        }
        return false;
    }

    private void checkInterrupted() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException("Thread " + Thread.currentThread() + " interrupted");
        }
    }
}
