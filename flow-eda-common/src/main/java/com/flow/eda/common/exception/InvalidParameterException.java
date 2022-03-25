package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class InvalidParameterException extends FlowException {

    public InvalidParameterException(String message) {
        super(ApiError.INVALID_PARAMETER, message);
        super.setHttpStatus(ApiError.BAD_REQUEST);
    }

    public InvalidParameterException(String name, Object value) {
        super(
                ApiError.INVALID_PARAMETER,
                String.format("The %s parameter value %s invalid", name, value));
        super.setHttpStatus(ApiError.BAD_REQUEST);
    }
}
