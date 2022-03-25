package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class InternalException extends FlowException {

    public InternalException(String message) {
        super(500, ApiError.INTERNAL_ERROR, message);
    }
}
