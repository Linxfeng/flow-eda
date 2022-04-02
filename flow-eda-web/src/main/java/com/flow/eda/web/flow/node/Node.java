package com.flow.eda.web.flow.node;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Node extends NodeType {
    private Long id;
    /** 节点属性 */
    private String nodeName;

    private String top;
    private String left;
    private String remark;
    private Map<String, Object> params;
    private Map<String, Object> input;
    private Map<String, Object> output;

    /** 连线属性 */
    private String label;

    private String from;
    private String to;
}
