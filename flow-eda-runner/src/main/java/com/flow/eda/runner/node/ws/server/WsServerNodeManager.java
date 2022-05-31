package com.flow.eda.runner.node.ws.server;

import com.flow.eda.common.exception.FlowException;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** WS服务端节点管理器 */
public class WsServerNodeManager {

    /** 管理节点path对应的节点实例 */
    private static final Map<String, WsServerNode> PATH_NODE = new ConcurrentHashMap<>();
    /** 管理节点id和节点path的对应关系 */
    private static final Map<String, String> ID_PATH = new ConcurrentHashMap<>();

    /**
     * 添加一个ws Endpoint
     *
     * @param node ws服务端节点实例
     */
    public static void addEndpoint(WsServerNode node) {
        String path = node.getPath();
        // 先校验是否已存在相同path
        if (containsPath(path)) {
            // 是否存在相同实例：若存在则更新节点实例，若不存在则抛出异常
            if (ID_PATH.containsKey(node.getNodeId())) {
                PATH_NODE.put(path, node);
                return;
            }
            throw new FlowException("The path '" + path + "' same node already exists");
        }
        PATH_NODE.put(path, node);
        ID_PATH.put(node.getNodeId(), path);
    }

    /**
     * 移除一个ws Endpoint
     *
     * @param node ws服务端节点实例
     */
    public static void removeEndpoint(WsServerNode node) {
        PATH_NODE.remove(node.getPath());
        ID_PATH.remove(node.getNodeId());
    }

    public static boolean containsPath(String path) {
        return PATH_NODE.containsKey(path);
    }

    /** 根据path获取节点实例 */
    @NonNull
    public static WsServerNode getNodeInstance(String path) {
        WsServerNode node = PATH_NODE.get(path);
        if (node == null) {
            throw new FlowException("The path '" + path + "' ws_server node not found");
        }
        return node;
    }
}
