package com.flow.eda.oauth2.oauth2User;

import com.flow.eda.oauth2.user.AuthProvider;
import com.flow.eda.oauth2.user.OauthUserMapper;
import com.flow.eda.oauth2.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SocialOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {



    private UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SocialOAuth2User user = (SocialOAuth2User) authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        if(userDetails==null){
             userDetailsService.saveNewSocialOAuth2User(user.getName(),user.getEmail(), AuthProvider.Github);
        }else {
             userDetailsService.updateSocialOAuth2User(user.getName(),user.getEmail(), AuthProvider.Github);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
