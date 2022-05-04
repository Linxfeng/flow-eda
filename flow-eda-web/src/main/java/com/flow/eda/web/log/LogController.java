package com.flow.eda.web.log;

import com.flow.eda.common.dubbo.model.Logs;
import com.flow.eda.common.exception.MissingRequestParameterException;
import com.flow.eda.web.http.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LogController {
    @Autowired private LogService logService;

    @OperationLog
    @GetMapping("/logs")
    public PageResult<Logs> logList(LogRequest request) {
        if (request.getType() == null) {
            throw new MissingRequestParameterException("type");
        }
        return PageResult.ofPage(logService.getLogList(request), request);
    }
}
