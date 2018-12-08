package com.vbl.distribution.server.controller;

import com.vbl.distribution.server.context.ConsumerContext;
import com.vbl.distribution.server.controller.common.LastMessage;
import com.vbl.distribution.server.event.DistributionEvent;
import com.vbl.distribution.server.service.OptionService;
import com.vbl.distirbution.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.stream.Stream;

public class OptionRequestController {

    @Autowired
    private OptionService optionServices;

    @LastMessage
    @EventListener(condition = "#event.message.messageType.name() == 'OPTION_REQUEST'")
    public void handleOptionRequest(DistributionEvent event) {
        ConsumerContext consumerContext = event.getConsumerContext();
        OptionRequest request = event.getMessage().getOptionRequest();

        Stream<Option> options;
        if (request.getIdentifiersCount() == 0) {
            options = optionServices.getAll();
        } else {
            options = request.getIdentifiersList()
                    .stream()
                    .flatMap(id -> optionServices.getByIdentifier(id.getType(), id.getValue()));
        }
        long resultsSent = options.map(opt -> project(opt, request.getSample()))
                .map(opt -> DistributionProtocol.newBuilder(event.getMessage())
                        .setMessageType(DistributionProtocol.MessageType.OPTION_RESPONSE)
                        .setOption(opt).build())
                .peek(consumerContext::send)
                .count();

        if (resultsSent == 0) {
            consumerContext.send(DistributionProtocol.newBuilder(event.getMessage())
                    .setMessageType(DistributionProtocol.MessageType.EMPTY)
                    .build());
        }
    }

    private static Option project(Option option, Option sample) {
        if (sample != Option.getDefaultInstance()) {
            Option.Builder builder = Option.newBuilder();
            sample.getAllFields().keySet().forEach(fieldDesc -> {
                builder.setField(fieldDesc, option.getField(fieldDesc));
            });
            return builder.build();
        } else {
            return option;
        }
    }

}
