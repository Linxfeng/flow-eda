package com.flow.eda.web.flow;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flow.eda.common.http.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowRequest extends PageRequest {
    private String name;
    private Boolean status;

    public LambdaQueryWrapper<Flow> selectWrapper() {
        LambdaQueryWrapper<Flow> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Flow::getStatus, status);
        }
        if (name != null) {
            wrapper.like(Flow::getName, name);
        }
        return wrapper;
    }
}
