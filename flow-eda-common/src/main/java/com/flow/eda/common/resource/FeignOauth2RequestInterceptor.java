package com.flow.eda.common.resource;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

/** 自定义Feign的请求拦截器，用于微服务之间调用时的oauth2认证 */
@Configuration
public class FeignOauth2RequestInterceptor implements RequestInterceptor {
    private static final String TOKEN_URL =
            "http://localhost:8086/oauth/token?grant_type=client_credentials";
    private static final String AUTH = "Authorization";
    private static final String BEARER = "Bearer ";

    @Value("${security.oauth2.client-id}")
    private String clientId;

    @Value("${security.oauth2.client-secret}")
    private String clientSecret;

    @Bean
    @ConditionalOnMissingClass
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 获取请求中的token，设置到header中
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details =
                    (OAuth2AuthenticationDetails) authentication.getDetails();
            requestTemplate.header(AUTH, BEARER + details.getTokenValue());
        } else {
            // 若请求中无token，则通过客户端模式向oauth2申请一个token，设置到header中
            String basic =
                    Base64.getEncoder()
                            .encodeToString(
                                    String.format("%s:%s", clientId, clientSecret).getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTH, "Basic " + basic);
            // 发送请求
            Document response =
                    restTemplate()
                            .exchange(
                                    TOKEN_URL,
                                    HttpMethod.POST,
                                    new HttpEntity<>(headers),
                                    Document.class)
                            .getBody();
            if (response != null) {
                requestTemplate.header(AUTH, BEARER + response.getString("access_token"));
            }
        }
    }
}
