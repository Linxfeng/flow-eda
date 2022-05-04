package com.flow.eda.web.log;

import com.flow.eda.common.dubbo.api.LogsService;
import com.flow.eda.web.http.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogRequest extends PageRequest {
    private LogsService.Type type;
}
