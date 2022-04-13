package com.flow.eda.runner.flow.node;

import com.flow.eda.common.dubbo.model.FlowData;
import com.flow.eda.common.exception.InternalException;
import com.flow.eda.runner.flow.node.end.EndNode;
import com.flow.eda.runner.flow.node.http.HttpNode;
import com.flow.eda.runner.flow.node.start.StartNode;
import com.flow.eda.runner.flow.node.timer.TimerNode;
import lombok.Getter;

@Getter
public enum NodeTypeEnum {
    /** 开始节点 */
    START("start", StartNode.class),
    /** 结束节点 */
    END("end", EndNode.class),
    /** 定时器 */
    TIMER("timer", TimerNode.class),
    HTTP("http", HttpNode.class),
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
