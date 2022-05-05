package com.flow.eda.web.log;

import com.flow.eda.common.dubbo.model.Logs;
import com.flow.eda.common.exception.MissingRequestParameterException;
import com.flow.eda.common.http.Result;
import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.web.http.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @OperationLog
    @DeleteMapping("/logs")
    public Result<String> deleteLogs(@RequestBody List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            logService.deleteLogs(ids);
        }
        return Result.ok();
    }
}
