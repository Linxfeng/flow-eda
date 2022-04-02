package com.flow.eda.web.flow.node;

import lombok.Data;

@Data
public class NodeType {
    private Long id;
    private String type;
    private String typeName;
    private String svg;
    private String background;
    private String description;
}
