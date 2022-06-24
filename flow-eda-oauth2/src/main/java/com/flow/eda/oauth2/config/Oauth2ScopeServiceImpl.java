package com.flow.eda.oauth2.config;

import com.flow.eda.common.dubbo.api.Oauth2ScopeService;
import com.flow.eda.common.utils.CollectionUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/** oauth2权限认证服务 */
@Service
@DubboService(interfaceClass = Oauth2ScopeService.class)
public class Oauth2ScopeServiceImpl implements Oauth2ScopeService {
    @Autowired private TokenStore tokenStore;

    @Override
    public boolean verifyPermissions(String token, String uri) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        if (oAuth2AccessToken != null) {
            OAuth2Authentication authentication = tokenStore.readAuthentication(oAuth2AccessToken);
            boolean expired = oAuth2AccessToken.isExpired();
            boolean authenticated = authentication.isAuthenticated();
            // 校验token有效
            if (!expired && authenticated) {
                // 校验请求对应的权限项
                List<String> authorities =
                        CollectionUtil.map(
                                authentication.getUserAuthentication().getAuthorities(),
                                GrantedAuthority::getAuthority);
                return WebSecurityConfig.uriMatchers(authorities, uri);
            }
        }
        return false;
    }
}
