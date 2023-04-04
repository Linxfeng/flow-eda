package com.flow.eda.common.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    /** 获取当前请求的IP地址 */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        return ip != null ? ip : request.getRemoteAddr();
    }
}
