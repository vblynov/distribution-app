package com.vbl.distribution.server.event;

import com.vbl.distribution.server.context.ConsumerContext;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class ConsumerCloseEvent extends ApplicationEvent {
    private final ConsumerContext consumerContext;

    public ConsumerCloseEvent(ConsumerContext consumerContext) {
        super("ConsumerCloseEvent");
        Objects.requireNonNull(consumerContext, "Consumer context is null");
        this.consumerContext = consumerContext;
    }

    public ConsumerContext getConsumerContext() {
        return consumerContext;
    }

}