package com.vbl.distribution.client;

import com.vbl.distirbution.model.*;
import com.vbl.distribution.client.response.ClientFuture;
import com.vbl.distribution.client.response.Response;

import java.util.function.Consumer;

/**
 * Distribution client API. All methods are assumed to run asynchronously unless noted otherwise
 */
public interface Client {

    /**
     * Send the login request to server
     *
     * @param loginRequest client {@link LoginRequest}
     * @return {@link ClientFuture} containing {@link LoginResponse}
     */
    ClientFuture<LoginResponse> login(LoginRequest loginRequest);

    /**
     * Send the search request for {@link Option}
     *
     * @param searchRequest search request
     * @param handler       to process responses (one invocation per response message)
     * @return {@link ClientFuture} to track completion
     */
    ClientFuture<Response> get(OptionRequest searchRequest, Consumer<Option> handler);

    /**
     * Send the search request for {@link Stock}
     * @param searchRequest search request
     * @param handler to process responses (one invocation per response message)
     * @return {@link ClientFuture} to track completion
     */
    ClientFuture<Response> get(StockRequest searchRequest, Consumer<Stock> handler);

    /**
     * Close client and release system resources. This method is assumed to be synchronous
     */
    void close();

}
