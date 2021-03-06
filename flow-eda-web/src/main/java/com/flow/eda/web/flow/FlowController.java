package com.flow.eda.web.flow;

import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.common.exception.MissingPropertyException;
import com.flow.eda.common.http.Result;
import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.web.http.PageResult;
import com.flow.eda.web.log.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FlowController {
    @Autowired private FlowService flowService;

    @OperationLog
    @GetMapping("/flow")
    public PageResult<Flow> listFlow(FlowRequest request, Principal principal) {
        String name = principal.getName();
        request.setUsername("admin".equals(name) ? null : name);
        return PageResult.of(flowService.listFlowByPage(request));
    }

    @OperationLog
    @PostMapping("/flow")
    public Result<Flow> addFlow(@RequestBody Flow flow, Principal principal) {
        this.check(flow);
        flow.setUsername(principal.getName());
        flowService.addFlow(flow);
        return Result.of(flow);
    }

    @OperationLog
    @PutMapping("/flow")
    public Result<Flow> updateFlow(@RequestBody Flow flow) {
        this.check(flow);
        return Result.of(flowService.updateFlow(flow));
    }

    @OperationLog
    @DeleteMapping("/flow")
    public Result<String> deleteFlow(@RequestBody List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            flowService.deleteFlow(ids);
        }
        return Result.ok();
    }

    private void check(Flow flow) {
        if (flow == null || flow.getId() == null) {
            throw new MissingPropertyException("id");
        }
        if (!StringUtils.hasText(flow.getName())) {
            throw new InvalidParameterException("name", "null");
        }
    }
}
