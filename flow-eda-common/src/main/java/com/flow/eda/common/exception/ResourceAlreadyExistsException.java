package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends FlowException {

    public ResourceAlreadyExistsException(String type, Object value) {
        super(
                ApiError.RESOURCE_ALREADY_EXISTS,
                String.format("Request %s %s already exists", type, value));
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public ResourceAlreadyExistsException(String type) {
        super(ApiError.RESOURCE_ALREADY_EXISTS, String.format("Request %s already exists", type));
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
