package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class InvalidStateException extends FlowException {
    public InvalidStateException(String message) {
        super(ApiError.BAD_REQUEST, ApiError.INVALID_STATE, message);
    }
}
