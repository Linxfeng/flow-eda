package com.flow.eda.web.flow;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flow.eda.common.exception.ResourceNotFoundException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements FlowService {
    @Autowired private FlowMapper flowMapper;

    public List<Flow> listFlowByPage(FlowRequest request) {
        PageHelper.startPage(request.getPage(), request.getLimit());
        return this.list(request.selectWrapper());
    }

    public void addFlow(Flow flow) {
        flow.setStatus(true);
        flow.setCreateDate(new Date());
        flow.setUpdateDate(new Date());
        flowMapper.insert(flow);
    }

    public Flow findById(long id) {
        Flow flow = this.getById(id);
        if (flow == null) {
            throw new ResourceNotFoundException("id", id);
        }
        return flow;
    }

    public Flow updateFlow(Flow flow) {
        flow.setUpdateDate(new Date());
        this.saveOrUpdate(flow);
        return findById(flow.getId());
    }

    public void deleteFlow(List<Long> ids) {
        this.removeBatchByIds(ids);
    }
}
