package com.flow.eda.web.flow.node.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.flow.eda.web.flow.node.type.NodeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("eda_flow_node_data")
@ApiModel(value = "EdaFlowNodeData对象", description = "")
public class NodeData {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "节点id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "节点名称")
    @TableField("node_name")
    private String nodeName;

    @ApiModelProperty(value = "流id")
    @TableField("flow_id")
    private Long flowId;

    @ApiModelProperty(value = "节点类型id")
    @TableField("type_id")
    private Long typeId;

    @ApiModelProperty(value = "当前节点位置top")
    @TableField("top")
    private String top;

    @ApiModelProperty(value = "当前节点位置left")
    @TableField("left")
    private String left;

    @ApiModelProperty(value = "节点备注信息")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "节点属性参数")
    @TableField("params")
    private String params;

    @ApiModelProperty(value = "连线起始点id")
    @TableField("from")
    private String from;

    @ApiModelProperty(value = "连线结束点id")
    @TableField("to")
    private String to;

    @ApiModelProperty(value = "节点类型")
    @TableField(exist = false)
    private NodeType nodeType;
}
