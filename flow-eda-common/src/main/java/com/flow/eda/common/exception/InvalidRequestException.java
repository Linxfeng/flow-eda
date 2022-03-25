package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class InvalidRequestException extends FlowException {

    public InvalidRequestException(String message) {
        super(ApiError.INVALID_REQUEST, message);
        super.setHttpStatus(ApiError.BAD_REQUEST);
    }
}
