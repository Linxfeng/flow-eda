package com.flow.eda.common.dubbo.model;

import lombok.Getter;

import java.io.Serializable;

/** 日志信息对象 */
@Getter
public class Logs implements Serializable {
    /** 日志文件路径 */
    private String path;
    /** 日志产生日期（文件名） */
    private String date;
    /** 流程信息 */
    private String flow;
    /** 日志文件大小（单位kb） */
    private long size;

    public Logs(String date) {
        this(date, null);
    }

    public Logs(String date, String flow) {
        if (date != null) {
            // 截掉文件名后缀
            this.date = date.substring(0, 10);
        }
        this.flow = flow;
    }

    public Logs fileInfo(String path, long size) {
        if (flow != null) {
            this.path = path + "/" + flow + "/" + date + ".log";
        } else {
            this.path = path + "/" + date + ".log";
        }
        this.size = size / 1024;
        return this;
    }

    /** 将原始的流程id信息替换为流程名称 */
    public void setFlowName(String flow) {
        this.flow = flow;
    }
}
