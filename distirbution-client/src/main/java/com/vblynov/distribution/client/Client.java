package com.vblynov.distribution.client;

import com.vblynov.distirbution.model.*;

public interface Client {

    //TODO consider API with promise result
    void login(LoginRequest loginRequest, ResponseHandler<LoginResponse> handler);

    void get(OptionRequest searchRequest, ResponseHandler<Option> handler);

    void get(StockRequest searchRequest, ResponseHandler<Stock> handler);

    void close();

}
