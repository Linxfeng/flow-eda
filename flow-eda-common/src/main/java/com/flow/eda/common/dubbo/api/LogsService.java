package com.flow.eda.common.dubbo.api;

import com.flow.eda.common.dubbo.model.Logs;

import java.util.List;

/** 日志服务接口 */
public interface LogsService {

    /**
     * 获取日志信息列表
     *
     * @param type 日志类型
     * @return 日志信息列表
     */
    List<Logs> getLogList(Type type);

    /** 日志类型 */
    enum Type {
        /** 操作日志 */
        OPERATION,
        /** 运行日志 */
        RUNNING
    }
}
