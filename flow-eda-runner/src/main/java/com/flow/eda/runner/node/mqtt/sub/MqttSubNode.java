package com.flow.eda.runner.node.mqtt.sub;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.mqtt.MqttAbstractNode;
import com.flow.eda.runner.runtime.FlowBlockNodePool;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/** MQTT订阅节点 */
public class MqttSubNode extends MqttAbstractNode implements FlowBlockNodePool.BlockNode {
    private NodeFunction callback;

    public MqttSubNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {

        setStatus(Status.RUNNING);
        this.callback = callback;

        try {
            // 建立MQTT连接
            MqttClient client = createMqttClient();
            // 设置回调
            client.setCallback(new MqttMessageCallback(this));
            // 订阅topic
            client.subscribe(getTopic());
        } catch (MqttException e) {
            throw new FlowException(e.getMessage());
        }
    }

    @Override
    protected void verify(Document params) {
        super.verify(params);
    }

    /** 回调，收到消息后向下游节点输出 */
    public void callback(String message) {
        this.callback.callback(output().append("message", message));
    }

    @Override
    public void destroy() {
        // 断开MQTT客户端连接，关闭会话
        super.closeMqttClient();
    }
}
