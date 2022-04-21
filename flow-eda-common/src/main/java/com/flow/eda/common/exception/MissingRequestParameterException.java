package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import org.springframework.http.HttpStatus;

public class MissingRequestParameterException extends FlowException {

    public MissingRequestParameterException(String name) {
        super(
                ApiError.MISSING_PARAMETER,
                String.format("Missing required request parameter %s", name));
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
