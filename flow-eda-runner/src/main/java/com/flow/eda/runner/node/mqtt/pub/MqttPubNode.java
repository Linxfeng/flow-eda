package com.flow.eda.runner.node.mqtt.pub;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.node.mqtt.MqttAbstractNode;
import com.flow.eda.runner.node.mqtt.MqttClientManager;
import com.flow.eda.runner.runtime.FlowBlockNodePool;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/** MQTT发布节点 */
public class MqttPubNode extends MqttAbstractNode implements FlowBlockNodePool.BlockNode {
    private String message;

    public MqttPubNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        try {
            // 建立MQTT连接
            MqttClientManager.createMqttClient(this);

            // 发布消息
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(0);
            getClient().publish(getTopic(), mqttMessage);

            // 输出发生的消息内容
            setStatus(Status.FINISHED);
            callback.callback(output().append("message", message));
        } catch (MqttException e) {
            throw new FlowException(e.getMessage());
        }
    }

    @Override
    protected void verify(Document params) {
        super.verify(params);
        try {
            // 校验topic，禁止通配符
            MqttTopic.validate(getTopic(), false);
        } catch (Exception e) {
            throw new FlowException(e.getMessage());
        }
        this.message = params.getString("message");
        NodeVerify.notBlank(message, "message");
    }

    @Override
    public void destroy() {
        // 断开MQTT连接，关闭会话
        MqttClientManager.closeMqttClient(this);
    }

    @Override
    public boolean eq(FlowBlockNodePool.BlockNode blockNode) {
        // MQTT发布节点可多次重复执行，复用MQTT连接，每次更新节点对象
        if (blockNode instanceof MqttAbstractNode) {
            MqttAbstractNode node = (MqttAbstractNode) blockNode;
            return getNodeId().equals(node.getNodeId());
        }
        return false;
    }
}
