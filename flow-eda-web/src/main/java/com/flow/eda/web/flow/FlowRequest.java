package com.flow.eda.web.flow;

import com.flow.eda.web.http.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowRequest extends PageRequest {
    private String name;
    private Flow.Status status;
}
