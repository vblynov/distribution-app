package com.vblynov.distribution.client;

import com.vblynov.distirbution.model.Error;

public interface ResponseHandler<T> {

    void onResponse(T response);

    void onError(Error error);

}
