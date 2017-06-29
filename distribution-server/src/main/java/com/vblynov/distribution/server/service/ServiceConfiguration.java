package com.vblynov.distribution.server.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public OptionService optionService() {
        return new OptionService();
    }

    @Bean
    public StockService stockService() {
        return new StockService();
    }

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService();
    }

}
