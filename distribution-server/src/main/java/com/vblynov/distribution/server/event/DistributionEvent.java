package com.vblynov.distribution.server.event;

import com.vblynov.distirbution.model.DistributionProtocol;
import com.vblynov.distribution.server.context.ConsumerContext;
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
