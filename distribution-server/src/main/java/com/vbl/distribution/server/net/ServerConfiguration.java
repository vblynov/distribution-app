package com.vbl.distribution.server.net;

import com.vbl.distirbution.model.DistributionProtocol;
import io.netty.channel.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
