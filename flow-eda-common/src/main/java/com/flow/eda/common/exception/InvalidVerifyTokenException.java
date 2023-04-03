package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class InvalidVerifyTokenException extends FlowException {

    public InvalidVerifyTokenException(String message) {
        super(ApiError.INVALID_VERIFY, message);
    }
}
