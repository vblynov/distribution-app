package com.vbl.distribution.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for sending a heartbeat message when channel idle state detected
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(HeartbeatHandler.class);

    private static final ByteBuf HEARTBEAT_SEQUENCE =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                    "HEARTBEAT", CharsetUtil.ISO_8859_1));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            LOG.debug("Sending heartbeat to client {}", ctx.channel().id().asLongText());
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(HEARTBIT_FAILURE_LISTENER);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private static final ChannelFutureListener HEARTBIT_FAILURE_LISTENER = future -> {
        if (!future.isSuccess()) {
            LOG.warn("Heartbeat failed for channel {}", future.channel().id().asLongText());
            future.channel().close();
        }
    };
}
