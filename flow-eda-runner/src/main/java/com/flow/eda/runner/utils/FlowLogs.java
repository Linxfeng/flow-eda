package com.flow.eda.runner.utils;

import lombok.extern.slf4j.Slf4j;

/** 流程运行日志工具，用于统一管理流程运行日志，并发送到mq中 */
@Slf4j
public class FlowLogs {
    private static final String SEPARATOR = "#flowId:";

    public static void info(String flowId, String msg, Object... args) {
        log.info(msg + SEPARATOR + flowId, args);
    }

    public static void error(String flowId, String msg, Object... args) {
        log.error(msg + SEPARATOR + flowId, args);
    }
}
