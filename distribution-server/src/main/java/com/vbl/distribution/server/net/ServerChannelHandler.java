package com.vbl.distribution.server.net;

import com.vbl.distribution.server.context.ConsumerContext;
import com.vbl.distribution.server.context.ConsumerRepository;
import com.vbl.distribution.server.event.ConsumerCloseEvent;
import com.vbl.distribution.server.event.DistributionEvent;
import com.vbl.distirbution.model.DistributionProtocol;
import com.vbl.distirbution.model.Error;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import com.vbl.distirbution.model.DistributionProtocol.MessageType;

import java.util.Optional;

@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<DistributionProtocol> {
    private static Logger LOG = LoggerFactory.getLogger(ServerChannelHandler.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DistributionProtocol msg) {
        LOG.info("Received message {}", msg);
        if (msg.getMessageType() == MessageType.LOGIN_REQUEST) {
            if (StringUtils.isEmpty(msg.getToken())) {
                applicationContext.publishEvent(new DistributionEvent(msg, new ConsumerContext(ctx.channel(), getChannelId(ctx.channel()))));
            }
        } else {
            String token = msg.getToken();
            if (StringUtils.isEmpty(token)) {
                ctx.channel().writeAndFlush(DistributionProtocol
                        .newBuilder(msg)
                        .setMessageType(MessageType.ERROR)
                        .setErrorMessage(Error.newBuilder().setMessage("Unauthorized").build())
                        .build()
                );
            } else {
                consumerRepository.getConsumerContext(token)
                        .ifPresent(consumer -> applicationContext.publishEvent(new DistributionEvent(msg, consumer)));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Optional<ConsumerContext> consumerContextOptional = consumerRepository.getConsumerContext(getChannelId(ctx.channel()));
        if (consumerContextOptional.isPresent()) {
            ConsumerContext consumerContext = consumerContextOptional.get();
            LOG.error("Exception from channel " + getChannelId(ctx.channel()) + " for consumer " + consumerContext.getConsumerName(), cause);
        } else {
            LOG.error("Exception from channel " + getChannelId(ctx.channel()), cause);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        final String channelId = getChannelId(ctx.channel());
        LOG.info("Unregistering consumer channel with token {}", channelId);
        consumerRepository.getConsumerContext(channelId)
                .ifPresent(consumer -> applicationContext.publishEvent(new ConsumerCloseEvent(consumer)));

    }

    private static String getChannelId(Channel channel) {
        return channel.id().asLongText();
    }

}
