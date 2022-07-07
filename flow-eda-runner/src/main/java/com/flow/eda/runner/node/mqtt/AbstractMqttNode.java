package com.flow.eda.runner.node.mqtt;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeVerify;
import lombok.Getter;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Getter
public abstract class AbstractMqttNode extends AbstractNode {
    private String clientId;
    private String broker;
    private String topic;
    private String username;
    private String password;
    private MqttClient client;

    public AbstractMqttNode(Document params) {
        super(params);
    }

    /** 建立MQTT连接 */
    protected void createMqttClient() {
        try {
            this.client = new MqttClient(getBroker(), getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            // 临时会话
            options.setCleanSession(true);
            // 掉线重连
            options.setAutomaticReconnect(true);
            if (username != null) {
                options.setUserName(username);
            }
            if (password != null) {
                options.setPassword(password.toCharArray());
            }
            // 建立连接
            client.connect(options);
        } catch (MqttException e) {
            throw new FlowException(e.getMessage());
        }
    }

    protected void setClient(MqttClient client) {
        this.client = client;
    }

    protected void closeMqttClient() {
        if (this.client != null) {
            try {
                this.client.disconnect();
                this.client.close();
            } catch (MqttException ignored) {
            }
        }
    }

    @Override
    protected void verify(Document params) {
        this.clientId = params.getString("clientId");
        NodeVerify.notBlank(clientId, "clientId");

        String broker = params.getString("broker");
        NodeVerify.notNull(broker, "broker");

        NodeVerify.isTrue(broker.contains(","), "broker");
        String host = broker.split(",")[0];
        NodeVerify.notNull(host, "broker");

        String port = broker.split(",")[1];
        NodeVerify.notNull(port, "broker");
        this.broker = host + ":" + port;

        this.topic = params.getString("topic");
        NodeVerify.notBlank(topic, "topic");

        this.username = params.getString("username");
        this.password = params.getString("password");
    }
}
