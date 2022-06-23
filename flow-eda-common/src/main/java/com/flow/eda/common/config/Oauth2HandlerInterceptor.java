package com.flow.eda.common.config;

import com.flow.eda.common.dubbo.api.Oauth2ScopeService;
import com.flow.eda.common.http.Result;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Oauth2权限认证 请求拦截器 */
@Component
public class Oauth2HandlerInterceptor implements HandlerInterceptor {
    @DubboReference private Oauth2ScopeService oauth2ScopeService;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null) {
            // 校验该token是否有对应的API访问权限
            boolean pass =
                    oauth2ScopeService.verifyPermissions(
                            token.replaceFirst("Bearer ", ""), request.getRequestURI());
            if (pass) {
                return true;
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter()
                .write(Result.failed(HttpStatus.UNAUTHORIZED.getReasonPhrase()).toJson());
        return false;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView)
            throws Exception {}

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {}
}
