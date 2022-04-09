package com.flow.eda.web.flow.node.type;

import com.flow.eda.web.flow.node.type.param.NodeTypeParam;
import lombok.Data;

import java.util.List;

/** 节点类型 */
@Data
public class NodeType {
    private Long id;
    /** 节点类型 */
    private String type;
    /** 节点类型名称 */
    private String typeName;
    /** 节点图标 */
    private String svg;
    /** 节点背景色 */
    private String background;
    /** 节点类型描述 */
    private String description;
    /** 节点参数 */
    private List<NodeTypeParam> param;
}
