package com.flow.eda.common.http;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    public static final String INTERNAL_ERROR = "internal_error";
    public static final String REQUEST_NOT_ALLOWED = "request_not_allowed";
    public static final String MISSING_PARAMETER = "missing_parameter";
    public static final String MISSING_PROPERTY_IN_BODY = "missing_property_in_body";
    public static final String RESOURCE_NOT_FOUND = "resource_not_found";
    public static final String RESOURCE_ALREADY_EXISTS = "resource_already_exists";
    public static final String INVALID_REQUEST = "invalid_request";
    public static final String INVALID_PARAMETER = "invalid_parameter";
    public static final String INVALID_VERIFY = "invalid_verify_token";
    public static final String INVALID_STATE = "invalid_state";

    private String error;
    private int status = 200;
    private String message;
    private String path;
    private String description;

    public ApiError error(String error) {
        this.error = error;
        return this;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ApiError description(String description) {
        this.description = description;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiError message(String message) {
        this.message = message;
        return this;
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        try {
            return HttpStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return HttpStatus.OK;
        }
    }

    @JsonIgnore
    public boolean hasError() {
        return this.error != null;
    }

    @JsonGetter
    public Integer getStatus() {
        if (status == 0 || status == 200) {
            return null;
        }
        return status;
    }

    public ApiError status(int status) {
        this.status = status;
        return this;
    }
}
