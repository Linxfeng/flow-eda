package com.flow.eda.runner.node.ws.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 自定义HandshakeInterceptor拦截器，用于建立ws握手前拦截过滤不匹配的请求 */
@Slf4j
public class WsHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private static final Map<String, WsServerNode> PATH_MAP = new ConcurrentHashMap<>();

    /**
     * 添加一个ws Endpoint
     *
     * @param node ws服务端节点
     */
    public static void addEndpoint(WsServerNode node) {
        PATH_MAP.put(node.getPath(), node);
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes)
            throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            String path = serverHttpRequest.getURI().getPath();
            log.info("WebSocketInterceptor path: {}", path);
            if (PATH_MAP.containsKey(path)) {
                return true;
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
