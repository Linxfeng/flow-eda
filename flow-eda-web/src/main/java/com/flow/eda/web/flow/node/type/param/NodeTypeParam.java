package com.flow.eda.web.flow.node.type.param;

import lombok.Data;

/** 节点类型参数定义 */
@Data
public class NodeTypeParam {
    private Long id;
    private Long typeId;
    private String key;
    private String name;
    private boolean required;
    /** 参数输入类型: input | select */
    private String inType = "input";
    /** 下拉选项内容，多个值以逗号分隔 */
    private String option;
    /** 参数值提示性内容 */
    private String placeholder;
}
