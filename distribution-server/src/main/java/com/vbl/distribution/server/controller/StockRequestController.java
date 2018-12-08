package com.vbl.distribution.server.controller;

import com.vbl.distribution.server.controller.common.LastMessage;
import com.vbl.distirbution.model.DistributionProtocol;
import com.vbl.distirbution.model.Stock;
import com.vbl.distirbution.model.StockRequest;
import com.vbl.distribution.server.context.ConsumerContext;
import com.vbl.distribution.server.event.DistributionEvent;
import com.vbl.distribution.server.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class StockRequestController {

    @Autowired
    private StockService stockServices;

    @LastMessage
    @EventListener(condition = "#event.message.messageType.name() == 'STOCK_REQUEST'")
    public void handleStockRequest(DistributionEvent event) {
        ConsumerContext consumerContext = event.getConsumerContext();
        StockRequest request = event.getMessage().getStockRequest();

        List<Stock> stocks = stockServices.getByIdentifier(request.getIdentifierType(), request.getValue());
        if (CollectionUtils.isEmpty(stocks)) {
            consumerContext.send(DistributionProtocol.newBuilder(event.getMessage())
                    .setMessageType(DistributionProtocol.MessageType.EMPTY)
                    .build());
        } else {
            stocks.stream()
                    .map(stock -> DistributionProtocol.newBuilder(event.getMessage())
                            .setMessageType(DistributionProtocol.MessageType.STOCK_RESPONSE)
                            .setStock(stock).build())
                    .forEach(consumerContext::send);

        }
    }
}
