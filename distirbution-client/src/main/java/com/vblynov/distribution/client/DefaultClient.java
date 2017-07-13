package com.vblynov.distribution.client;

import com.vblynov.distirbution.model.*;
import com.vblynov.distribution.client.exception.ServerException;
import com.vblynov.distribution.client.response.ClientFuture;
import com.vblynov.distribution.client.response.ClientFutureListener;
import com.vblynov.distribution.client.response.Response;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

class DefaultClient implements Client {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultClient.class);

    private final Map<String, Consumer> registeredCallbacks = new ConcurrentHashMap<>();
    private final Map<String, ClientPromise> registeredPromise = new ConcurrentHashMap<>();
    private final Channel channel;
    private final EventLoopGroup workerGroup;
    private String token;

    DefaultClient(Channel channel, EventLoopGroup workerGroup) {
        this.channel = channel;
        this.workerGroup = workerGroup;
    }

    @Override
    public ClientFuture<LoginResponse> login(LoginRequest loginRequest) {
        DistributionProtocol.Builder builder = prepareMessage(DistributionProtocol.MessageType.LOGIN_REQUEST);
        DefaultClientPromise<LoginResponse> promise = new DefaultClientPromise<>(channel.write(builder.setLoginRequest(loginRequest).build()));
        registeredPromise.putIfAbsent(builder.getCorrelationId(), promise);
        channel.flush();
        return promise;
    }

    @Override
    public ClientFuture<Response> get(OptionRequest searchRequest, Consumer<Option> handler) {
        DistributionProtocol.Builder builder = prepareMessage(DistributionProtocol.MessageType.OPTION_REQUEST);
        registeredCallbacks.putIfAbsent(builder.getCorrelationId(), handler);
        DefaultClientPromise<Response> promise = new DefaultClientPromise<>(channel.write(builder.setOptionRequest(searchRequest).build()));
        registeredPromise.putIfAbsent(builder.getCorrelationId(), promise);
        channel.flush();
        return promise;
    }

    @Override
    public ClientFuture<Response> get(StockRequest searchRequest, Consumer<Stock> handler) {
        DistributionProtocol.Builder builder = prepareMessage(DistributionProtocol.MessageType.STOCK_REQUEST);
        registeredCallbacks.putIfAbsent(builder.getCorrelationId(), handler);
        DefaultClientPromise<Response> promise = new DefaultClientPromise<>(channel.write(builder.setStockRequest(searchRequest).build()));
        registeredPromise.putIfAbsent(builder.getCorrelationId(), promise);
        return promise;
    }

    @Override
    public void close() {
        try {
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            LOG.error("Error during client shutdown ", e);
        } finally {
            registeredCallbacks.clear();
            LOG.info("Client disconnected");
        }
    }

    private DistributionProtocol.Builder prepareMessage(DistributionProtocol.MessageType type) {
        String correlationId = String.valueOf(System.nanoTime());
        DistributionProtocol.Builder builder = DistributionProtocol.newBuilder()
                .setMessageType(type)
                .setCorrelationId(correlationId);
        if (token != null) {
            builder.setToken(token);
        }
        return builder;
    }

    void processResponse(DistributionProtocol msg) {
        Consumer callback = registeredCallbacks.getOrDefault(msg.getCorrelationId(), NULL_CONSUMER);
        ClientPromise promise = registeredPromise.getOrDefault(msg.getCorrelationId(), NULL_PROMISE);
        switch (msg.getMessageType()) {
            case ERROR:
                promise.setFailure(new ServerException(msg.getErrorMessage()));
                break;
            case LOGIN_RESPONSE:
                token = msg.getToken();
                promise.setSuccess(msg.getLoginResponse());
                registeredPromise.remove(msg.getCorrelationId());
                break;
            case OPTION_RESPONSE:
                callback.accept(msg.getOption());
                break;
            case STOCK_RESPONSE:
                callback.accept(msg.getStock());
                break;
            case LAST:
                promise.setSuccess(Response.DONE);
                registeredCallbacks.remove(msg.getCorrelationId());
                registeredPromise.remove(msg.getCorrelationId());
                break;
        }
    }

    private static final Consumer NULL_CONSUMER = new Consumer() {
        @Override
        public void accept(Object o) {

        }

        @Override
        public Consumer andThen(Consumer after) {
            return null;
        }
    };

    private static final ClientPromise NULL_PROMISE = new ClientPromise() {
        @Override
        public void setSuccess(Object result) {

        }

        @Override
        public void setFailure(Throwable cause) {

        }

        @Override
        public ClientFuture listener(ClientFutureListener listener) {
            return null;
        }

        @Override
        public void await() throws InterruptedException {

        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException, TimeoutException {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public Object get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    };
}
