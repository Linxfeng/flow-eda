package com.flow.eda.common.resource;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

/** 自定义限流异常 */
public class RateLimitExceededException extends ClientAuthenticationException {

    public RateLimitExceededException(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        return 403;
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "rate_limit_exceeded";
    }
}
