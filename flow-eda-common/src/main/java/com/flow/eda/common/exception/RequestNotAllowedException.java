package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class RequestNotAllowedException extends FlowException {

    public RequestNotAllowedException(String message) {
        super(HttpStatus.BAD_REQUEST, ApiError.REQUEST_NOT_ALLOWED, message);
    }
}
