package com.flow.eda.runner.node;

import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.common.exception.InternalException;
import com.flow.eda.runner.node.delay.DelayNode;
import com.flow.eda.runner.node.http.HttpNode;
import com.flow.eda.runner.node.output.OutputNode;
import com.flow.eda.runner.node.parser.ParserNode;
import com.flow.eda.runner.node.start.StartNode;
import com.flow.eda.runner.node.timer.TimerNode;
import lombok.Getter;

@Getter
public enum NodeTypeEnum {
    /** 开始节点 */
    START("start", StartNode.class),
    /** 定时器 */
    TIMER("timer", TimerNode.class),
    HTTP("http", HttpNode.class),
    OUTPUT("output", OutputNode.class),
    PARSER("parser", ParserNode.class),
    DELAY("delay", DelayNode.class),
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
