package com.flow.eda.common.exception;

import com.flow.eda.common.http.ApiError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FlowException extends RuntimeException {
    private String error;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message;
    private List<ApiError.SubError> errors;

    public FlowException(String message) {
        this.message = message;
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
    }

    public static FlowException wrap(Throwable e) {
        return wrap(e, ApiError.INTERNAL_ERROR);
    }

    public static FlowException wrap(Throwable e, String message) {
        if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
        }
        if (e instanceof FlowException) {
            return (FlowException) e;
        }
        return new FlowException(message);
    }

    public ApiError getApiError() {
        return new ApiError().error(this.error).status(httpStatus.value()).message(getMessage());
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

    public int getStatus() {
        return this.httpStatus.value();
    }
}
