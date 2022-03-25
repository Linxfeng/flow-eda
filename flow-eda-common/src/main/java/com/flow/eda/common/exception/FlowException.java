package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FlowException extends RuntimeException {
    private String error;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message;
    private List<ApiError.SubError> errors;
    private String description;

    public FlowException(String error) {
        this.error = error;
    }

    public FlowException(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public FlowException(HttpStatus httpStatus, String error, String message) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.message = message;
    }

    public FlowException(ApiError error) {
        this.error = error.getError();
        this.message = error.getMessage();
        this.errors = error.getErrors();
        this.httpStatus = error.getHttpStatus();
        this.description = error.getDescription();
    }

    public ApiError getApiError() {
        return new ApiError()
                .error(this.error)
                .status(httpStatus.value())
                .message(getMessage())
                .description(description);
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return super.getMessage();
        }
        return message;
    }

    public FlowException setMessage(String message) {
        this.message = message;
        return this;
    }

    public FlowException description(String description) {
        this.description = description;
        return this;
    }
}
