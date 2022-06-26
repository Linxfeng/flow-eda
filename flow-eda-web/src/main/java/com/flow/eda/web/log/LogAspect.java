package com.flow.eda.web.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LogAspect {
    private static final String GET = "GET";

    @Autowired private ObjectMapper objectMapper;

    public static void error(String message) {
        log.error(message);
    }

    @Pointcut("@annotation(com.flow.eda.web.log.OperationLog)")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = Instant.now().toEpochMilli();
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        Object proceed = point.proceed();
        long end = Instant.now().toEpochMilli();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 获取请求参数
        String args;
        if (GET.equals(request.getMethod())) {
            String s = request.getQueryString();
            args = s == null ? "" : s;
        } else {
            args = getArgsString(point.getArgs());
        }

        log.info(
                "USER: {}, URI: {}, METHOD: {} {}, IP: {}, STATUS: {}, TIME: {}, ARGS: {}",
                auth.getName(),
                request.getRequestURI(),
                request.getMethod(),
                point.getSignature().getName(),
                getIp(request),
                response.getStatus(),
                end - start,
                args);
        return proceed;
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void afterThrowing(Throwable e) {
        HttpServletRequest request = getRequest();
        int status = 500;
        if (e instanceof FlowException) {
            status = ((FlowException) e).getStatus();
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.error(
                "USER: {}, URI: {}, METHOD: {}, IP: {}, STATUS: {}, ERROR: {}",
                auth.getName(),
                request.getRequestURI(),
                request.getMethod(),
                getIp(request),
                status,
                e.getMessage());
    }

    @NonNull
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        throw new InternalException("can not get HttpServletRequest");
    }

    @NonNull
    private HttpServletResponse getResponse() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletResponse response = attributes.getResponse();
            if (response != null) {
                return response;
            }
        }
        throw new InternalException("can not get HttpServletResponse");
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        return ip != null ? ip : request.getRemoteAddr();
    }

    /** 获取参数内容，截取前55位 */
    private String getArgsString(Object[] args) {
        try {
            if (args != null && args.length > 0) {
                int length = 55;
                String s = objectMapper.writeValueAsString(args[0]);
                if (s.length() > length) {
                    return s.substring(0, length) + "...";
                }
                return s;
            }
        } catch (Exception ignored) {
        }
        return "";
    }
}
