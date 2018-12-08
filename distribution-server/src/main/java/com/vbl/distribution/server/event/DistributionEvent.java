package com.vbl.distribution.server.event;

import com.vbl.distribution.server.context.ConsumerContext;
import com.vbl.distirbution.model.DistributionProtocol;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class DistributionEvent extends ApplicationEvent {

    private final DistributionProtocol message;
    private final ConsumerContext consumerContext;

    public DistributionEvent(DistributionProtocol message, ConsumerContext consumerContext) {
        super("DistributionEvent");
        Objects.requireNonNull(message, "Message is null");
        Objects.requireNonNull(consumerContext, "Consumer context is null");
        this.message = message;
        this.consumerContext = consumerContext;
    }

    public DistributionProtocol getMessage() {
        return message;
    }

    public ConsumerContext getConsumerContext() {
        return consumerContext;
    }
}
