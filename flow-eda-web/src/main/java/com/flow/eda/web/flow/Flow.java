package com.flow.eda.web.flow;

import lombok.Data;

import java.util.Date;

@Data
public class Flow {
    private long id;
    private String name;
    private String description;
    private boolean status;
    private Date createDate;
    private Date updateDate;
}
