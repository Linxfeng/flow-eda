package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class InvalidVerifyTokenException extends FlowException {

    public InvalidVerifyTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, ApiError.INVALID_VERIFY, message);
    }
}
