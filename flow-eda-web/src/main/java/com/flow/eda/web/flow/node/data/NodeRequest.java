package com.flow.eda.web.flow.node.data;

import com.flow.eda.web.http.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeRequest extends PageRequest {
    private String name;
}
