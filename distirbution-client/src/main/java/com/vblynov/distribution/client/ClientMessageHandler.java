package com.vblynov.distribution.client;

import com.vblynov.distirbution.model.DistributionProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ClientMessageHandler extends SimpleChannelInboundHandler<DistributionProtocol> {
    private static final Logger LOG = LoggerFactory.getLogger(ClientMessageHandler.class);

    private DefaultClient currentClient;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DistributionProtocol msg) throws Exception {
        currentClient.processResponse(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.error("Excepton thrown ", cause);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOG.info("Client channel closed");
        if (currentClient.isClientActive()) {
            currentClient.doClose();
        }
    }

    void setClient(DefaultClient client) {
        this.currentClient = client;
    }

}