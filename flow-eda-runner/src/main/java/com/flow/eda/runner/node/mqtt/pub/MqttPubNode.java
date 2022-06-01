package com.flow.eda.runner.node.mqtt.pub;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.node.mqtt.MqttAbstractNode;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/** MQTT发布节点 */
public class MqttPubNode extends MqttAbstractNode {
    private String message;

    public MqttPubNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.RUNNING);
        try {
            // 建立MQTT连接
            MqttClient client = createMqttClient();

            // 发布消息
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(0);
            client.publish(getTopic(), mqttMessage);

            // 输出发生的消息内容
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
}
