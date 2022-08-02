package com.flow.eda.oauth2.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 自定义限流过滤器 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    /** guava令牌桶算法实现限流，每秒5个请求 */
    private final RateLimiter limiter = RateLimiter.create(5.0);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // 获取不到令牌，立即进入服务降级
        if (!limiter.tryAcquire()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Rate limit exceeded");
            // throw new AccessDeniedException("Rate limit exceeded");
        }
        filterChain.doFilter(request, response);
    }
}
