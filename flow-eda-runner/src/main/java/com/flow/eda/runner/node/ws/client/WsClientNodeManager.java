package com.flow.eda.runner.node.ws.client;

import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** WS客户端节点管理器 */
public class WsClientNodeManager {

    /** 管理节点id对应的ws连接 */
    private static final Map<String, WebSocketConnectionManager> WS_MAP = new ConcurrentHashMap<>();

    /** 创建一个ws客户端连接 */
    public static void createWebSocketClient(WsClientNode node) {
        WebSocketConnectionManager manager =
                new WebSocketConnectionManager(
                        new StandardWebSocketClient(),
                        new WsClientNodeHandler(node),
                        node.getUri());
        manager.start();
        WS_MAP.put(node.getNodeId(), manager);
    }

    /** 关闭一个ws客户端连接 */
    public static void closeWebSocketClient(WsClientNode node) {
        String id = node.getNodeId();
        if (contains(id)) {
            WS_MAP.get(id).stop();
            WS_MAP.remove(id);
        }
    }

    public static boolean contains(String id) {
        return WS_MAP.containsKey(id);
    }
}
