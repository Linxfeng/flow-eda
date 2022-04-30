package com.flow.eda.runner.flow.utils;

import lombok.extern.slf4j.Slf4j;

/** 流程运行日志工具，用于统一管理流程运行日志，并发送到mq中 */
@Slf4j
public class FlowLogs {

    public static void info(String flowId, String msg, Object... args) {
        log.info(msg + "#flowId:" + flowId, args);
    }

    public static void error(String flowId, String msg, Object... args) {
        log.error(msg + "#flowId:" + flowId, args);
    }
}
