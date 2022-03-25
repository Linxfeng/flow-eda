package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class InternalException extends FlowException {

    public InternalException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, ApiError.INTERNAL_ERROR, message);
    }
}
