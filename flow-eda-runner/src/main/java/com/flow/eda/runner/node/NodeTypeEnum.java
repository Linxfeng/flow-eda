package com.flow.eda.runner.node;

import com.flow.eda.common.exception.InternalException;
import com.flow.eda.common.model.FlowData;
import com.flow.eda.runner.node.delay.DelayNode;
import com.flow.eda.runner.node.http.request.HttpRequestNode;
import com.flow.eda.runner.node.http.response.HttpResponseNode;
import com.flow.eda.runner.node.output.OutputNode;
import com.flow.eda.runner.node.parser.ParserNode;
import com.flow.eda.runner.node.start.StartNode;
import com.flow.eda.runner.node.timer.TimerNode;
import com.flow.eda.runner.node.ws.client.WsClientNode;
import com.flow.eda.runner.node.ws.server.WsServerNode;
import lombok.Getter;

/***
 * 节点类型
 */
@Getter
public enum NodeTypeEnum {
    /** 基础节点 */
    START("start", StartNode.class),
    OUTPUT("output", OutputNode.class),
    TIMER("timer", TimerNode.class),
    DELAY("delay", DelayNode.class),
    /** 解析节点 */
    PARSER("parser", ParserNode.class),
    /** 网络节点 */
    HTTP_REQUEST("http_request", HttpRequestNode.class),
    HTTP_RESPONSE("http_response", HttpResponseNode.class),
    WS_SERVER("ws_server", WsServerNode.class),
    WS_CLIENT("ws_client", WsClientNode.class),
    ;
    private final String type;
    private final Class<? extends Node> clazz;

    NodeTypeEnum(String type, Class<? extends Node> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public static NodeTypeEnum getEnumByType(String type) {
        for (NodeTypeEnum anEnum : values()) {
            if (anEnum.getType().equals(type)) {
                return anEnum;
            }
        }
        return null;
    }

    public static Class<? extends Node> getClazzByNode(FlowData node) {
        NodeTypeEnum typeEnum = getEnumByType(node.getType());
        if (typeEnum == null) {
            throw new InternalException("cannot find node type " + node.getType());
        }
        return typeEnum.getClazz();
    }
}
