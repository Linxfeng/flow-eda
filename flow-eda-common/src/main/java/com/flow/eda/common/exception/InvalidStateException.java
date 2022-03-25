package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class InvalidStateException extends FlowException {
    public InvalidStateException(String message) {
        super(HttpStatus.BAD_REQUEST, ApiError.INVALID_STATE, message);
    }
}
