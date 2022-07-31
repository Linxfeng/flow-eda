package com.flow.eda.oauth2.config;

import com.flow.eda.common.http.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 自定义限流过滤器 */
//@Component
public class RateLimitFilter implements Filter {
    /** 同个IP每秒最大访问量 */
    private static final Integer MAX_VISIT = 3;
    /** 请求IP对应的请求次数 */
    private final Map<String, Integer> visitMap = new ConcurrentHashMap<>(4);

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String ip = this.getIp(request);
            int visitCount = 1;
            if (this.visitMap.containsKey(ip)) {
                visitCount = this.visitMap.get(ip) + 1;
                if (visitCount >= MAX_VISIT) {
                    this.forbiddenRequest(servletResponse);
                    return;
                }
            }
            this.visitMap.put(ip, visitCount);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /** 禁止访问 */
    private void forbiddenRequest(ServletResponse servletResponse) throws IOException {
        if (servletResponse instanceof HttpServletResponse) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter()
                    .write(Result.failed("Server is busy! Please try again later").toJson());
        }
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        return ip != null ? ip : request.getRemoteAddr();
    }

    @Override
    public void destroy() {}
}
