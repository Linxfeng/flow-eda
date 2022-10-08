package com.flow.eda.oauth2.config;

import com.flow.eda.oauth2.handler.LoginFailureHandler;
import com.flow.eda.oauth2.handler.LoginSuccessHandler;
import com.flow.eda.oauth2.handler.LogoutSuccessHandler;
import com.flow.eda.oauth2.oauth2User.SocialOAuth2UserService;
import com.flow.eda.oauth2.oauth2User.SocialOAuthSuccessHandler;
import com.flow.eda.oauth2.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Lazy @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private LoginSuccessHandler loginSuccessHandler;
    @Autowired private LoginFailureHandler loginFailureHandler;
    @Autowired private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired private RateLimitFilter rateLimitFilter;

    @Autowired
    private SocialOAuth2UserService socialOAuth2UserService;

    @Autowired
    private SocialOAuthSuccessHandler SocialSuccessHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests().antMatchers("/oauth2/**")
                        .permitAll().antMatchers("/oauth/**").authenticated()
                        .anyRequest().permitAll().and()

                .formLogin()
                .loginProcessingUrl("/oauth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .oauth2Login().loginProcessingUrl("/oauth/login").
                userInfoEndpoint().userService(socialOAuth2UserService)
                .and()
                .successHandler(SocialSuccessHandler)
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutUrl("/oauth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .and()
                .httpBasic();
        // 单用户登录
        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(true);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenExtractor tokenExtractor() {
        return new BearerTokenExtractor();
    }
}
