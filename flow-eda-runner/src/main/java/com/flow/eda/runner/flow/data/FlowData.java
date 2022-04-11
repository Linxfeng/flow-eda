package com.flow.eda.runner.flow.data;

import lombok.Data;
import org.bson.Document;

/** 运行时节点数据 */
@Data
public class FlowData {
    private String id;
    /** 流id */
    private Long flowId;
    /** 节点类型 */
    private String type;
    /** 节点属性参数 */
    private Document params;
    /** 连线起始点id */
    private String from;
    /** 连线结束点id */
    private String to;
}
