package com.vblynov.distribution.client;

import com.vblynov.distirbution.model.*;
import com.vblynov.distribution.client.response.ClientFuture;
import com.vblynov.distribution.client.response.Response;

import java.util.function.Consumer;

public interface Client {

    ClientFuture<LoginResponse> login(LoginRequest loginRequest);

    ClientFuture<Response> get(OptionRequest searchRequest, Consumer<Option> handler);

    ClientFuture<Response> get(StockRequest searchRequest, Consumer<Stock> handler);

    void close();

}
