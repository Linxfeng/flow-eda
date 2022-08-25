package com.flow.eda.oauth2.config;

import com.flow.eda.common.http.Result;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/** 自定义限流过滤器 */
@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    /** guava令牌桶算法实现限流，限制每个IP每秒10个请求 */
    private final LoadingCache<String, RateLimiter> limiterLoadingCache =
            CacheBuilder.newBuilder()
                    .maximumSize(200)
                    .expireAfterWrite(1, TimeUnit.MINUTES)
                    .build(
                            new CacheLoader<String, RateLimiter>() {
                                @Override
                                public RateLimiter load(String s) {
                                    return RateLimiter.create(10.0);
                                }
                            });

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String ip = this.getIp(request);
        try {
            boolean acquire = this.limiterLoadingCache.get(ip).tryAcquire();
            // 获取不到令牌，立即进入服务降级
            if (!acquire) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write(Result.failed("Rate limit exceeded").toJson());
                return;
            }
        } catch (Exception e) {
            log.error("get limiterLoadingCache error: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        return ip != null ? ip : request.getRemoteAddr();
    }
}
