package com.flow.eda.oauth2.config;

import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.oauth2.handler.LoginFailureHandler;
import com.flow.eda.oauth2.handler.LoginSuccessHandler;
import com.flow.eda.oauth2.handler.LogoutSuccessHandler;
import com.flow.eda.oauth2.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.cors.CorsUtils;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String URI_API = "/api/v1/";
    private static final String URI_WS = "/ws/";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_API = "ROLE_API";
    private static final String ROLE_WS = "ROLE_WS";

    @Lazy @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private LoginSuccessHandler loginSuccessHandler;
    @Autowired private LoginFailureHandler loginFailureHandler;
    @Autowired private LogoutSuccessHandler logoutSuccessHandler;

    /** 检验请求的uri是否含有对应需要的权限项 */
    static boolean uriMatchers(List<String> authorities, String uri) {
        if (CollectionUtil.isEmpty(authorities)) {
            return false;
        }
        // admin权限全部放行
        if (authorities.contains(ROLE_ADMIN)) {
            return true;
        }
        if (uri.startsWith(URI_API)) {
            return authorities.contains(ROLE_API);
        }
        if (uri.startsWith(URI_WS)) {
            return authorities.contains(ROLE_WS);
        }
        // 除了配置的uri之外的请求全部放行，系统找不到会自动404
        return true;
    }

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
        http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                // 处理跨域请求中的Preflight请求
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll()
                .antMatchers("/oauth/**")
                .permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/oauth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .logout()
                .logoutUrl("/oauth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(true);
    }
}
