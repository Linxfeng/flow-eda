package com.flow.eda.common.config;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.http.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.sasl.AuthenticationException;

@Slf4j
@ControllerAdvice
public class FlowExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleFlowException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        if (ex instanceof FlowException) {
            ApiError error = ((FlowException) ex).getApiError();
            return handleExceptionInternal2(ex, error, headers, error.getHttpStatus(), request);
        } else if (ex instanceof AuthenticationException) {
            return handleAuthenticationException(((AuthenticationException) ex), headers, request);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return handleHttpMessageNotReadable(
                    (HttpMessageNotReadableException) ex, headers, HttpStatus.BAD_REQUEST, request);
        }
        log.error("Catch error!", ex);
        return handleExceptionInternal2(
                ex, null, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, HttpHeaders headers, WebRequest request) {
        ApiError error = new ApiError().error(ex.getMessage());
        return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError fieldError = ex.getFieldError();
        if (fieldError != null) {
            String field = fieldError.getField();
            String description =
                    String.format("Request parameter %s %s", field, fieldError.getDefaultMessage());
            ApiError error =
                    new ApiError().error(ApiError.INVALID_PARAMETER).description(description);
            String code = fieldError.getCode();
            if (code != null) {
                switch (code) {
                    case "NotEmpty":
                    case "NotNull":
                        error.error(ApiError.MISSING_PARAMETER).setMessage(description);
                        break;
                    default:
                        String message =
                                String.format("The %s parameter value type invalid", field);
                        error.message(message).setDescription(description);
                }
            }
            return handleExceptionInternal2(ex, error, headers, status, request);
        }
        return handleExceptionInternal2(ex, null, headers, status, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal2(
            Exception ex,
            ApiError apiError,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        if (apiError == null) {
            apiError = new ApiError();
            apiError.message(status.getReasonPhrase());
        }
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        if (body == null) {
            String error = ApiError.INTERNAL_ERROR;
            if (status == HttpStatus.BAD_REQUEST) {
                error = ApiError.INVALID_REQUEST;
            }
            ApiError apiError = new ApiError();
            body = apiError.error(error).message(ex.getMessage());
            if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                ex.printStackTrace();
            }
        }
        if (body instanceof ApiError) {
            ApiError error = (ApiError) body;
            if (error.getPath() == null) {
                error.setPath(getRequestUri(request));
            }
            error.setStatus(status.value());
            if (error.getMessage() == null) {
                error.setMessage(ex.getMessage());
            }
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        ApiError apiError =
                new ApiError().error(ApiError.INVALID_PARAMETER).message(ex.getMessage());
        if (fieldError != null) {
            String code = fieldError.getCode();
            if (code != null) {
                switch (code) {
                    case "NotEmpty":
                    case "NotNull":
                        apiError.error(ApiError.MISSING_PROPERTY_IN_BODY);
                    default:
                }
            }
            String field = fieldError.getField();
            String message =
                    String.format("Request parameter %s %s", field, fieldError.getDefaultMessage());
            apiError.message(message);
        }
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        String message = String.format("Missing '%s' parameter value", ex.getParameterName());
        ApiError error = new ApiError().error(ApiError.MISSING_PARAMETER).message(message);
        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String name = ex.getPropertyName();
        if (ex instanceof MethodArgumentTypeMismatchException) {
            name = ((MethodArgumentTypeMismatchException) ex).getName();
        }
        String message = String.format("The %s parameter value type invalid", name);
        ApiError error = new ApiError().error(ApiError.INVALID_PARAMETER).message(message);
        return handleExceptionInternal2(ex, error, headers, status, request);
    }

    @Nullable
    private String getRequestUri(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            return ((ServletWebRequest) request).getRequest().getRequestURI();
        }
        return null;
    }
}
