package com.flow.eda.common.http;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    public static final int HTTP_OK = 200;
    public static final int BAD_REQUEST = 400;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String error;
    private int status = 200;
    private String message;
    private List<SubError> errors;
    private String path;
    private String description;

    public static ApiError parse(String source) {
        try {
            return OBJECT_MAPPER.readValue(source, ApiError.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("invalid json object to convert to ApiError");
        }
    }

    public static ApiError parse(byte[] source) {
        try {
            return OBJECT_MAPPER.readValue(source, ApiError.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid json object to convert to ApiError");
        }
    }

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
    public int getHttpStatus() {
        return status;
    }

    @JsonIgnore
    public boolean hasError() {
        return this.error != null;
    }

    @JsonGetter
    public Integer getStatus() {
        if (status == 0 || status == HTTP_OK) {
            return null;
        }
        return status;
    }

    public ApiError status(int status) {
        this.status = status;
        return this;
    }

    @Data
    public static class SubError {
        private String field;
        private String error;
    }
}
