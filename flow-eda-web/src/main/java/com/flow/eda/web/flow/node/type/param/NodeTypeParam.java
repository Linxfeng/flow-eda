package com.flow.eda.web.flow.node.type.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** 节点类型参数定义 */
@Data
@TableName("eda_flow_node_type_param")
@ApiModel(value = "EdaFlowNodeTypeParam对象", description = "")
public class NodeTypeParam {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "节点类型id")
    @TableField("type_id")
    private Long typeId;

    @ApiModelProperty(value = "参数key")
    @TableField("key")
    private String key;

    @ApiModelProperty(value = "参数名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "参数是否必填")
    @TableField("required")
    private Boolean required;

    @ApiModelProperty(value = "参数输入类型")
    @TableField("in_type")
    private String inType = "input";

    @ApiModelProperty(value = "下拉选项内容，多个值以逗号分隔")
    @TableField("option")
    private String option;

    @ApiModelProperty(value = "参数值提示性内容")
    @TableField("placeholder")
    private String placeholder;
}
