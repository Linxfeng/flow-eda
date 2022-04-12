package com.flow.eda.web.flow.node.type;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.flow.eda.web.flow.node.type.param.NodeTypeParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/** 节点类型 */
@Data
@TableName("eda_flow_node_type")
@ApiModel(value = "EdaFlowNodeType对象", description = "")
public class NodeType {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "节点类型")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "名称")
    @TableField("type_name")
    private String typeName;

    @ApiModelProperty(value = "图标")
    @TableField("svg")
    private String svg;

    @ApiModelProperty(value = "背景色")
    @TableField("background")
    private String background;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = " 节点参数 ")
    @TableField(exist = false)
    private List<NodeTypeParam> params;
}
