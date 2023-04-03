package com.flow.eda.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AccessTokenConfig {
    @Autowired private DataSource dataSource;

    @Value("${flow.oauth2.jwt_signing_key}")
    private String jwtSigningKey;

    /** Access Token持久化 */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /** 声明ClientDetailsService实现，jdbc方式 */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /** JWT转换器 */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSigningKey);
        return converter;
    }

    /** 自定义TokenEnhancerChain */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(delegates);
        return tokenEnhancerChain;
    }

    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // 设置ClientDetailsService为jdbc实现方式
        tokenServices.setClientDetailsService(jdbcClientDetailsService());
        // 设置token存储方式
        tokenServices.setTokenStore(tokenStore());
        // 设置自定义TokenEnhancer
        tokenServices.setTokenEnhancer(tokenEnhancerChain());
        // 允许支持refreshToken
        tokenServices.setSupportRefreshToken(true);
        // 不重用refreshToken
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }
}
