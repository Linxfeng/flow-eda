package com.flow.eda.web.flow;

import com.flow.eda.common.http.PageResult;
import com.flow.eda.common.http.Result;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1")
public class FlowController {

    @GetMapping("/flow")
    public PageResult<Flow> listFlow() {
        return PageResult.of(new ArrayList<>());
    }

    @PostMapping("/flow")
    public Result<Flow> addFlow() {
        return Result.of(null);
    }

    @PutMapping("/flow")
    public Result<Flow> updateFlow() {
        return Result.of(null);
    }

    @DeleteMapping("/flow")
    public Result<Flow> deleteFlow() {
        return Result.of(null);
    }
}
