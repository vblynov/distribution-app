package com.vbl.distribution.server;

import com.vbl.distribution.server.controller.ControllerConfiguration;
import com.vbl.distribution.server.net.Server;
import com.vbl.distribution.server.net.ServerConfiguration;
import com.vbl.distribution.server.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ServerConfiguration.class, ControllerConfiguration.class, ServiceConfiguration.class})
public class DistributionServer {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DistributionServer.class, args);
        applicationContext.registerShutdownHook();

        Server server = applicationContext.getBean(Server.class);
        server.start();
    }
}
