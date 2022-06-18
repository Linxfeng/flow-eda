package com.flow.eda.oauth2.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class OauthUserDetails implements UserDetails {

    private final OauthUser oauthUser;

    public OauthUserDetails(OauthUser oauthUser) {
        this.oauthUser = oauthUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(oauthUser.getAuthorities());
    }

    @Override
    public String getPassword() {
        return oauthUser.getPassword();
    }

    @Override
    public String getUsername() {
        return oauthUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return oauthUser.getStatus();
    }

    @Override
    public boolean isAccountNonLocked() {
        return oauthUser.getStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return oauthUser.getStatus();
    }

    @Override
    public boolean isEnabled() {
        return oauthUser.getStatus();
    }
}
