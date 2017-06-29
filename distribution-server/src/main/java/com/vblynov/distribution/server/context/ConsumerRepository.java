package com.vblynov.distribution.server.context;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConsumerRepository {

    private ConcurrentHashMap<String, ConsumerContext> activeConsumers = new ConcurrentHashMap<>();

    public Optional<ConsumerContext> getConsumerContext(String token) {
        return Optional.ofNullable(activeConsumers.get(token));
    }

    public void put(ConsumerContext consumerContext) {
        activeConsumers.putIfAbsent(consumerContext.getToken(), consumerContext);
    }

    public void remove(String token) {
        activeConsumers.remove(token);
    }
}
