package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;

public class MissingRequestParameterException extends FlowException {

    public MissingRequestParameterException(String name) {
        super(
                ApiError.MISSING_PARAMETER,
                String.format("Missing required request parameter %s", name));
    }
}
