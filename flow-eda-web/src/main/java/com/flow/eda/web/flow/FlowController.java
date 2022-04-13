package com.flow.eda.web.flow;

import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.common.exception.MissingPropertyException;
import com.flow.eda.common.http.Result;
import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.web.flow.http.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FlowController {
    @Autowired private FlowService flowService;

    @GetMapping("/flow")
    public PageResult<Flow> listFlow(FlowRequest request) {
        return PageResult.of(flowService.listFlowByPage(request));
    }

    @PostMapping("/flow")
    public Result<Flow> addFlow(@RequestBody Flow flow) {
        this.checkName(flow);
        flowService.addFlow(flow);
        log.info("Create flow {}", flow.getName());
        return Result.of(flow);
    }

    @PutMapping("/flow")
    public Result<Flow> updateFlow(@RequestBody Flow flow) {
        this.check(flow);
        log.info("Update flow {}", flow.getId());
        return Result.of(flowService.updateFlow(flow));
    }

    @DeleteMapping("/flow")
    public Result<String> deleteFlow(@RequestBody List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            flowService.deleteFlow(ids);
        }
        return Result.ok();
    }

    private void check(Flow flow) {
        if (flow == null || flow.getId() == null) {
            throw new MissingPropertyException("id");
        }
        this.checkName(flow);
    }

    private void checkName(Flow flow) {
        if (flow == null || !StringUtils.hasText(flow.getName())) {
            throw new InvalidParameterException("name", "null");
        }
    }
}
