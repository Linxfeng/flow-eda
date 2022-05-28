package com.flow.eda.runner.node.ws.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/** 自定义HandshakeInterceptor拦截器，用于建立ws握手前拦截过滤不匹配的请求 */
@Slf4j
public class WsHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /** 拦截请求，请求路径不在PATH_MAP内的拒绝连接 */
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;

            String path = httpRequest.getURI().getPath();
            return WsServerNodeManager.containsPath(path);
        }

        return false;
    }
}
