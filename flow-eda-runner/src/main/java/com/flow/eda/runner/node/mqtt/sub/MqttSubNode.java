package com.flow.eda.runner.node.mqtt.sub;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.mqtt.MqttAbstractNode;
import com.flow.eda.runner.node.mqtt.MqttClientManager;
import com.flow.eda.runner.runtime.FlowBlockNodePool;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;

/** MQTT订阅节点 */
public class MqttSubNode extends MqttAbstractNode implements FlowBlockNodePool.BlockNode {
    private NodeFunction callback;

    public MqttSubNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        // 校验是否重复连接
        if (MqttClientManager.contains(this)) {
            throw new FlowException("The mqtt_sub node can not run repeatedly");
        }
        if (MqttClientManager.contains(getClientId())) {
            throw new FlowException(
                    "The clientId '" + getClientId() + "' same node already exists");
        }

        setStatus(Status.RUNNING);
        this.callback = callback;

        try {
            // 建立MQTT连接
            MqttClientManager.createMqttClient(this);
            // 设置回调
            getClient().setCallback(new MqttMessageCallback(this));
            // 订阅topic
            getClient().subscribe(getTopic());
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
        MqttClientManager.closeMqttClient(this);
    }
}
