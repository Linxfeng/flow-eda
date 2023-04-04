package com.flow.eda.oauth2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.flow.eda.oauth2.config.AccessTokenConfig.CACHE_NAME;

@Service
public class OauthUserService {

    @Autowired private OauthUserMapper oauthUserMapper;

    @Cacheable(value = CACHE_NAME, key = "#username + '-' + #clientId")
    public OauthUser loadUserByUsername(String username, String clientId) {
        return oauthUserMapper.loadUserByUsername(username, clientId);
    }

    @Cacheable(value = CACHE_NAME, key = "#username", unless = "#result == null")
    public String existUsername(String username) {
        return oauthUserMapper.existUsername(username);
    }

    public void insert(OauthUser oauthUser) {
        oauthUserMapper.insert(oauthUser);
    }
}
