package com.vblynov.distribution.client;

import com.vblynov.distirbution.model.*;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class DefaultClient implements Client {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultClient.class);

    private final Map<String, ResponseHandler> registeredCallbacks = new ConcurrentHashMap<>();
    private final Channel channel;
    private final EventLoopGroup workerGroup;
    private String token;

    DefaultClient(Channel channel, EventLoopGroup workerGroup) {
        this.channel = channel;
        this.workerGroup = workerGroup;
    }

    @Override
    public void login(LoginRequest loginRequest, ResponseHandler<LoginResponse> handler) {
        channel.writeAndFlush(prepareMessage(handler, DistributionProtocol.MessageType.LOGIN_REQUEST)
                .setLoginRequest(loginRequest).build());
    }

    @Override
    public void get(OptionRequest searchRequest, ResponseHandler<Option> handler) {
        channel.writeAndFlush(prepareMessage(handler, DistributionProtocol.MessageType.OPTION_REQUEST)
                .setOptionRequest(searchRequest).build());
    }

    @Override
    public void get(StockRequest searchRequest, ResponseHandler<Stock> handler) {
        channel.writeAndFlush(prepareMessage(handler, DistributionProtocol.MessageType.STOCK_REQUEST)
                .setStockRequest(searchRequest).build());
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

    private DistributionProtocol.Builder prepareMessage(ResponseHandler handler, DistributionProtocol.MessageType type) {
        String correlationId = String.valueOf(System.nanoTime());
        registeredCallbacks.put(correlationId, handler);
        DistributionProtocol.Builder builder = DistributionProtocol.newBuilder()
                .setMessageType(type)
                .setCorrelationId(correlationId);
        if (token != null) {
            builder.setToken(token);
        }
        return builder;
    }

    void processResponse(DistributionProtocol msg) {
        Optional.ofNullable(registeredCallbacks.get(msg.getCorrelationId()))
                .ifPresent(callback -> {
                    switch (msg.getMessageType()) {
                        case ERROR:
                            callback.onError(msg.getErrorMessage());
                            break;
                        case LOGIN_RESPONSE:
                            token = msg.getToken();
                            callback.onResponse(msg.getLoginResponse());
                            break;
                        case OPTION_RESPONSE:
                            callback.onResponse(msg.getOption());
                            break;
                        case STOCK_RESPONSE:
                            callback.onResponse(msg.getStock());
                            break;
                        case LAST:
                            registeredCallbacks.remove(msg.getCorrelationId());
                            break;
                    }
                });
    }
}
