package com.flow.eda.common.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    protected T result;
    protected String error;

    public Result(T result) {
        this.result = result;
    }

    public static Result<String> success() {
        return new Result<>("success");
    }

    public static Result<String> ok() {
        return new Result<>("ok");
    }

    public static <T> Result<T> ok(T value) {
        return of(value);
    }

    public static Result<String> failed() {
        return failed("");
    }

    public static Result<String> failed(String error) {
        Result<String> result = new Result<>();
        result.setError(error);
        return result;
    }

    public static <T> Result<T> of(T object) {
        return new Result<>(object);
    }
}
