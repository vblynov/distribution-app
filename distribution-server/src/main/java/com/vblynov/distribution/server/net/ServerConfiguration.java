package com.vblynov.distribution.server.net;

import com.vblynov.distirbution.model.DistributionProtocol;
import com.vblynov.distribution.server.context.ConsumerRepository;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class ServerConfiguration {

    @Bean
    public Server server() {
        Server server = new Server(serverChannelHandler());
        server.setBossThreads(5);
        server.setWorkerThreads(5);
        server.setServerPort(7777);
        return server;
    }

    @Bean
    public SimpleChannelInboundHandler<DistributionProtocol> serverChannelHandler() {
        return new ServerChannelHandler();
    }

}
