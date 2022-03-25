package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class ResourceNotFoundException extends FlowException {
    private static final int NOT_FOUND = 404;

    public ResourceNotFoundException(String type, Object value) {
        super(
                NOT_FOUND,
                ApiError.RESOURCE_NOT_FOUND,
                String.format("Requested %s %s not found", type, value));
    }

    public ResourceNotFoundException(String message) {
        super(NOT_FOUND, ApiError.RESOURCE_NOT_FOUND, message);
    }
}
