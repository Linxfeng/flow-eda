package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class RequestNotAllowedException extends FlowException {

    public RequestNotAllowedException(String message) {
        super(ApiError.BAD_REQUEST, ApiError.REQUEST_NOT_ALLOWED, message);
    }
}
