package com.flow.eda.runner.node.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** MQTT客户端连接管理器 */
public class MqttClientManager {
    /** 存储clientId和对应的Mqtt节点，便于重复利用客户端连接 */
    private static final Map<String, AbstractMqttNode> CLIENT_MAP = new ConcurrentHashMap<>();

    /** 创建MqttClient连接，若已存在则复用连接，若不存在则创建连接 */
    public static void createMqttClient(AbstractMqttNode node) {
        String clientId = node.getClientId();
        synchronized (CLIENT_MAP) {
            if (contains(clientId)) {
                MqttClient client = CLIENT_MAP.get(clientId).getClient();
                if (client != null && client.isConnected()) {
                    // 复用客户端连接
                    node.setClient(client);
                }
            } else {
                node.createMqttClient();
            }
            // 更新节点对象
            CLIENT_MAP.put(clientId, node);
        }
    }

    /** 关闭MqttClient连接 */
    public static void closeMqttClient(AbstractMqttNode node) {
        node.closeMqttClient();
        String clientId = node.getClientId();
        if (contains(clientId)) {
            CLIENT_MAP.remove(clientId);
        }
    }

    /** 是否包含相同clientId */
    public static boolean contains(String clientId) {
        return CLIENT_MAP.containsKey(clientId);
    }

    /** 是否包含相同节点 */
    public static boolean contains(AbstractMqttNode node) {
        if (contains(node.getClientId())) {
            return node.getNodeId().equals(CLIENT_MAP.get(node.getClientId()).getNodeId());
        }
        return false;
    }
}
