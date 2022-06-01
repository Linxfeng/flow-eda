package com.flow.eda.runner.node.mqtt.sub;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/** MQTT回调 */
@Slf4j
public class MqttMessageCallback implements MqttCallbackExtended {
    private final MqttSubNode node;

    public MqttMessageCallback(MqttSubNode node) {
        this.node = node;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("MQTT connection lost. cause:{}", throwable.getMessage());
    }

    @Override
    public void connectComplete(boolean b, String s) {
        if (b) {
            log.info("MQTT reconnected successfully");
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String msg = new String(message.getPayload());
        log.info("Received topic '{}' message: {}", topic, msg);
        node.callback(msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}
}
