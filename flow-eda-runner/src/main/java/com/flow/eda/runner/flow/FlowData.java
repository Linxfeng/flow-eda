package com.flow.eda.runner.flow;

import lombok.Data;

import java.util.Date;

@Data
public class FlowData {
    private long id;
    private String script;
    private int executeCount;
    private Date lastExecuteDate;
    private Date createDate;
    private Date updateDate;
}
