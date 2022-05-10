package com.flow.eda.web.log;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.lang.NonNull;
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
        log.info(
                "URI: {}, METHOD: {}, IP: {}, STATUS: {}, TIME: {}",
                request.getRequestURI(),
                request.getMethod(),
                getIp(request),
                response.getStatus(),
                end - start);
        return proceed;
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void afterThrowing(Throwable e) {
        HttpServletRequest request = getRequest();
        int status = 500;
        if (e instanceof FlowException) {
            status = ((FlowException) e).getStatus();
        }
        log.error(
                "URI: {}, METHOD: {}, IP: {}, STATUS: {}, ERROR: {}",
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
}
