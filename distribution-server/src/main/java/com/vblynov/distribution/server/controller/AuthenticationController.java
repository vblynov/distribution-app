package com.vblynov.distribution.server.controller;

import com.vblynov.distirbution.model.DistributionProtocol;
import com.vblynov.distirbution.model.LoginRequest;
import com.vblynov.distirbution.model.LoginResponse;
import com.vblynov.distribution.server.context.ConsumerRepository;
import com.vblynov.distribution.server.controller.common.LastMessage;
import com.vblynov.distribution.server.event.ConsumerCloseEvent;
import com.vblynov.distribution.server.event.DistributionEvent;
import com.vblynov.distribution.server.context.ConsumerContext;
import com.vblynov.distribution.server.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

public class AuthenticationController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @LastMessage
    @EventListener(condition = "#event.message.messageType.name() == 'LOGIN_REQUEST'")
    public void handleLoginRequest(DistributionEvent event) {
        LoginRequest loginRequest = event.getMessage().getLoginRequest();
        ConsumerContext consumerContext = event.getConsumerContext();
        String consumerName = authenticationService.authenticateConsumer(loginRequest);
        consumerContext.setConsumerName(consumerName);
        consumerRepository.put(consumerContext);
        LOG.info("Successful authorization for consumer {} with token {}", consumerContext.getConsumerName(), consumerContext.getToken());
        DistributionProtocol.Builder builder = DistributionProtocol
                .newBuilder(event.getMessage())
                .setMessageType(DistributionProtocol.MessageType.LOGIN_RESPONSE);
        LoginResponse.Builder loginResponseBuilder = LoginResponse.newBuilder().setResult(LoginResponse.LoginResult.OK);
        consumerContext.send(builder.setLoginResponse(loginResponseBuilder.build()).build());
    }

    @EventListener(ConsumerCloseEvent.class)
    public void handleConsumerClose(ConsumerCloseEvent event) {
        consumerRepository.remove(event.getConsumerContext().getToken());
        LOG.info("Consumer {} closed connection", event.getConsumerContext().getConsumerName());
    }
}
