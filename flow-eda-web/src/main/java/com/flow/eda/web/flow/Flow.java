package com.flow.eda.web.flow;

import lombok.Data;

import java.util.Date;

@Data
public class Flow {
    private String id;
    private String name;
    private String description;
    private Status status;
    private Date createDate;
    private Date updateDate;

    public enum Status {
        /** 未运行 */
        INIT,
        /** 运行中 */
        RUNNING,
        /** 运行完成 */
        FINISHED,
        /** 运行失败 */
        FAILED
    }
}
