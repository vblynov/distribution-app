package com.vblynov.distribution.server;

import com.vblynov.distribution.server.controller.ControllerConfiguration;
import com.vblynov.distribution.server.net.Server;
import com.vblynov.distribution.server.net.ServerConfiguration;
import com.vblynov.distribution.server.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
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
