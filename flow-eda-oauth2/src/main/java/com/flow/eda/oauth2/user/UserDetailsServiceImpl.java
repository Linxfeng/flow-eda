package com.flow.eda.oauth2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** 自定义UserDetailsService实现 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BasicAuthenticationConverter converter = new BasicAuthenticationConverter();
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private OauthUserMapper oauthUserMapper;
    @Autowired private JdbcClientDetailsService jdbcClientDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId;
        if (authentication != null && authentication.getPrincipal() != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OauthUserDetails) {
                getClientIdByRequest();
                return (OauthUserDetails) principal;
            } else if (principal instanceof User) {
                clientId = ((User) principal).getUsername();
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            clientId = getClientIdByRequest();
        }
        OauthUser oauthUser = oauthUserMapper.loadUserByUsername(username, clientId);
        if (oauthUser == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return new OauthUserDetails(oauthUser);
    }

    /** 从Request中获取并验证客户端信息 */
    private String getClientIdByRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new UnsupportedOperationException();
        }
        UsernamePasswordAuthenticationToken token = converter.convert(attributes.getRequest());
        if (token == null) {
            throw new UnauthorizedClientException("unauthorized client");
        }
        ClientDetails clientDetails =
                jdbcClientDetailsService.loadClientByClientId(token.getName());
        if (!passwordEncoder.matches(
                (String) token.getCredentials(), clientDetails.getClientSecret())) {
            throw new BadClientCredentialsException();
        }
        return clientDetails.getClientId();
    }
}
