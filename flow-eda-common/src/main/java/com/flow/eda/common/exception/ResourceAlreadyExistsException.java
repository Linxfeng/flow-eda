package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class ResourceAlreadyExistsException extends FlowException {

    public ResourceAlreadyExistsException(String type, Object value) {
        super(ApiError.RESOURCE_ALREADY_EXISTS);
        super.setHttpStatus(ApiError.BAD_REQUEST);
        super.setMessage(String.format("Request %s %s already exists", type, value));
    }

    public ResourceAlreadyExistsException(String type) {
        super(ApiError.RESOURCE_ALREADY_EXISTS);
        super.setHttpStatus(ApiError.BAD_REQUEST);
        super.setMessage(String.format("Request %s already exists", type));
    }
}
