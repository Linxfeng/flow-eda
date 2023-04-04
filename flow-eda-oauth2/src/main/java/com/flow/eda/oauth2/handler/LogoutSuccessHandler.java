package com.flow.eda.oauth2.handler;

import com.flow.eda.common.http.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 登出成功处理器 */
@Component
public class LogoutSuccessHandler
        implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    @Lazy @Autowired private ConsumerTokenServices consumerTokenServices;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null) {
            // 注销token
            token = token.replace("Bearer", "").trim();
            consumerTokenServices.revokeToken(token);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(Result.success().toJson());
    }
}
