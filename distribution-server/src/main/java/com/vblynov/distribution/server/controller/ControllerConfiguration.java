package com.vblynov.distribution.server.controller;

import com.vblynov.distribution.server.controller.common.ControllerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class ControllerConfiguration {

    @Bean
    public AuthenticationController authenticationController() {
        return new AuthenticationController();
    }

    @Bean
    public OptionRequestController searchRequestController() {
        return new OptionRequestController();
    }

    @Bean
    public StockRequestController stockRequestController() {
        return new StockRequestController();
    }

    @Bean
    public ControllerAspect controllerAspect() {
        return new ControllerAspect();
    }

}
