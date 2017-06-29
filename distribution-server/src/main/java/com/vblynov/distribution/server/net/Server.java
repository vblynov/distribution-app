package com.vblynov.distribution.server.net;

import com.vblynov.distirbution.model.DistributionProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;

public class Server {
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private int workerThreads = 5;
    private int bossThreads = 5;
    private int serverPort = 7777;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    private final ChannelInboundHandlerAdapter handler;

    public Server(ChannelInboundHandlerAdapter handler) {
        this.handler = handler;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void start() throws Exception {
        //TODO consider IdleStateHandler and heartbeat message
        //TODO move harcoded values to config file
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(bossThreads);
        workerGroup = new NioEventLoopGroup(workerThreads);
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ProtobufVarint32FrameDecoder());
                        pipeline.addLast(new ProtobufDecoder(DistributionProtocol.getDefaultInstance()));

                        pipeline.addLast(handler);

                        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                        pipeline.addLast(new ProtobufEncoder());
                    }
                });
        serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 100);

        serverBootstrap
                .bind(serverPort)
                .sync()
                .channel()
                .closeFuture()
                .sync();
    }

    @PreDestroy
    public void stop() {
        try {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully().sync();
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully().sync();
            }
        } catch (InterruptedException ex) {
            LOG.error("Shutdown interrupted ", ex);
        }
    }

}