package com.flow.eda.common.dubbo.model;

import lombok.Data;
import org.bson.Document;

import java.io.Serializable;

/** 运行时节点数据 */
@Data
public class FlowData implements Serializable {
    private String id;
    /** 流id */
    private String flowId;
    /** 节点类型 */
    private String type;
    /** 节点属性参数 */
    private Document params;
    /** 连线起始点id */
    private String from;
    /** 连线结束点id */
    private String to;
}
