package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class MissingPropertyException extends FlowException {

    public MissingPropertyException(String name) {
        super(ApiError.MISSING_PROPERTY_IN_BODY);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
        super.setMessage(String.format("Missing required property %s in request body", name));
    }
}
