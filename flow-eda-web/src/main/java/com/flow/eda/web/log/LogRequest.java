package com.flow.eda.web.log;

import com.flow.eda.web.http.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogRequest extends PageRequest {
    private Type type;

    /** 日志类型 */
    enum Type {
        /** 操作日志 */
        OPERATION,
        /** 运行日志 */
        RUNNING
    }
}
