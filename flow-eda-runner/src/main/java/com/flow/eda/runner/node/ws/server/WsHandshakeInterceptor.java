package com.flow.eda.runner.node.ws.server;

import com.flow.eda.common.exception.FlowException;
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
        String path = node.getPath();
        if (PATH_MAP.containsKey(path)) {
            throw new FlowException("The path '" + path + "' same node already exists");
        }
        PATH_MAP.put(node.getPath(), node);
    }

    /**
     * 移除一个ws Endpoint
     *
     * @param path ws服务端节点的路径
     */
    public static void removeEndpoint(String path) {
        PATH_MAP.remove(path);
    }

    /** 拦截请求，请求路径不在PATH_MAP内的拒绝连接 */
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes)
            throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
            String path = httpRequest.getURI().getPath();
            return PATH_MAP.containsKey(path);
        }
        return false;
    }
}
