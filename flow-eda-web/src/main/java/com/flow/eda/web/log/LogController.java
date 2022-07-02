package com.flow.eda.web.log;

import com.flow.eda.common.exception.InvalidVerifyTokenException;
import com.flow.eda.common.exception.MissingRequestParameterException;
import com.flow.eda.common.http.Result;
import com.flow.eda.common.model.Logs;
import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.web.http.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LogController {
    private static final String ADMIN = "admin";
    @Autowired private LogService logService;

    @OperationLog
    @GetMapping("/logs")
    public PageResult<Logs> logList(LogRequest request, Principal principal) {
        if (request.getType() == null) {
            throw new MissingRequestParameterException("type");
        }
        // 仅管理员用户可查看操作日志
        if (LogsService.Type.OPERATION.equals(request.getType())
                && !ADMIN.equals(principal.getName())) {
            throw new InvalidVerifyTokenException("Insufficient permissions");
        }
        String username = ADMIN.equals(principal.getName()) ? null : principal.getName();
        List<Logs> logList = logService.getLogList(request, username);
        return PageResult.ofPage(logList, request);
    }

    @OperationLog
    @DeleteMapping("/logs")
    public Result<String> deleteLogs(@RequestBody List<String> ids) {
        // 操作日志不允许删除
        List<String> list = CollectionUtil.filter(ids, id -> !id.startsWith("/logs/operation"));
        if (CollectionUtil.isNotEmpty(list)) {
            logService.deleteLogs(list);
        }
        return Result.ok();
    }
}
