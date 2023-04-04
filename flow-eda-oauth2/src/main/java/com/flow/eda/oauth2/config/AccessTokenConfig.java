package com.flow.eda.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AccessTokenConfig {
    public static final String CACHE_NAME = "oauth2-cache";
    private final AuthenticationKeyGenerator authenticationKeyGenerator =
            new DefaultAuthenticationKeyGenerator();
    @Autowired private DataSource dataSource;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${flow.oauth2.jwt_signing_key}")
    private String jwtSigningKey;

    /** 定义CacheManager */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_NAME);
    }

    /** JdbcTokenStore 添加缓存 */
    @Bean
    public TokenStore tokenStore() {
        Cache oauth2Cache = cacheManager().getCache(CACHE_NAME);
        Assert.notNull(oauth2Cache, "The oauth2Cache can not be null");

        return new JdbcTokenStore(dataSource) {
            @Override
            public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
                String authenticationKey = authenticationKeyGenerator.extractKey(authentication);
                // 先尝试从缓存中获取accessToken
                OAuth2AccessToken accessToken =
                        oauth2Cache.get(authenticationKey, OAuth2AccessToken.class);
                if (accessToken != null) {
                    return accessToken;
                }
                // 缓存中没有则从数据库中获取
                accessToken = super.getAccessToken(authentication);
                if (accessToken != null) {
                    // 存储到缓存中
                    oauth2Cache.put(authenticationKey, accessToken);
                }
                return accessToken;
            }

            @Override
            public void storeAccessToken(
                    OAuth2AccessToken token, OAuth2Authentication authentication) {
                String authenticationKey = authenticationKeyGenerator.extractKey(authentication);
                // 存储到数据库中
                super.storeAccessToken(token, authentication);
                // 存储到缓存中
                oauth2Cache.put(authenticationKey, token);
            }
        };
    }

    /** JdbcClientDetailsService 添加缓存 */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        Cache oauth2Cache = cacheManager().getCache(CACHE_NAME);
        Assert.notNull(oauth2Cache, "The oauth2Cache can not be null");

        JdbcClientDetailsService clientDetailsService =
                new JdbcClientDetailsService(dataSource) {
                    @Override
                    public ClientDetails loadClientByClientId(String clientId)
                            throws ClientRegistrationException {
                        // 先尝试从缓存中获取ClientDetails
                        ClientDetails clientDetails =
                                oauth2Cache.get(clientId, ClientDetails.class);
                        if (clientDetails != null) {
                            return clientDetails;
                        }
                        // 缓存中没有则从数据库中获取
                        clientDetails = super.loadClientByClientId(clientId);
                        if (clientDetails != null) {
                            // 存储到缓存中
                            oauth2Cache.put(clientId, clientDetails);
                        }
                        return clientDetails;
                    }
                };
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /** JWT转换器 */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称加密，设置私钥
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
