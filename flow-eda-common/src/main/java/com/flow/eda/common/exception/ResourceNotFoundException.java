package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends FlowException {

    public ResourceNotFoundException(String type, Object value) {
        super(
                HttpStatus.NOT_FOUND,
                ApiError.RESOURCE_NOT_FOUND,
                String.format("Requested %s %s not found", type, value));
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, ApiError.RESOURCE_NOT_FOUND, message);
    }
}
