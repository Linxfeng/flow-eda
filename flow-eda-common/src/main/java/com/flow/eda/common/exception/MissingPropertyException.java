package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class MissingPropertyException extends FlowException {

    public MissingPropertyException(String name) {
        super(
                ApiError.MISSING_PROPERTY_IN_BODY,
                String.format("Missing required property %s in request body", name));
    }
}
