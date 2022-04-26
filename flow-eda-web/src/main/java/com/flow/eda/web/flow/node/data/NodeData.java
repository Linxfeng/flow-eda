package com.flow.eda.web.flow.node.data;

import com.flow.eda.web.flow.node.type.NodeType;
import lombok.Data;
import org.bson.Document;

@Data
public class NodeData {
    private String id;
    /** 节点名称 */
    private String nodeName;
    /** 流id */
    private String flowId;
    /** 节点类型id */
    private Long typeId;
    /** 当前节点位置top */
    private String top;
    /** 当前节点位置left */
    private String left;
    /** 节点备注信息 */
    private String remark;
    /** 节点属性参数 */
    private Document params;
    /** 自定义参数 */
    private Document payload;
    /** 节点类型 */
    private NodeType nodeType;
    /** 连线起始点id */
    private String from;
    /** 连线结束点id */
    private String to;
}
