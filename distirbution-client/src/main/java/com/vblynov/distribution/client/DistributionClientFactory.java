package com.vblynov.distribution.client;

import com.vblynov.distirbution.model.DistributionProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class DistributionClientFactory {

    public static Client createClient(String serverHost, int port) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(2);
        ClientMessageHandler messageHandler = new ClientMessageHandler();
        Bootstrap bootstrap =
                new Bootstrap()
                        .group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();

                                pipeline.addLast(new ProtobufVarint32FrameDecoder());
                                pipeline.addLast(new ProtobufDecoder(DistributionProtocol.getDefaultInstance()));

                                pipeline.addLast(messageHandler);

                                pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                                pipeline.addLast(new ProtobufEncoder());
                            }
                        });
        ChannelFuture channelFuture = bootstrap.connect(serverHost, port).sync();
        DefaultClient client = new DefaultClient(channelFuture.channel(), workerGroup);
        messageHandler.setClient(client);
        return client;
    }

}
