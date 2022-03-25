package com.flow.eda.web.flow;

import com.flow.eda.common.exception.ResourceNotFoundException;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FlowService {
    @Autowired private FlowMapper flowMapper;

    public List<Flow> listFlowByPage(FlowRequest request) {
        PageHelper.startPage(request.getPage(), request.getLimit());
        return flowMapper.findByRequest(request);
    }

    public void addFlow(Flow flow) {
        flow.setStatus(true);
        flow.setCreateDate(new Date());
        flow.setUpdateDate(new Date());
        flowMapper.insert(flow);
    }

    public Flow findById(long id) {
        Flow flow = flowMapper.findById(id);
        if (flow == null) {
            throw new ResourceNotFoundException("id", id);
        }
        return flow;
    }

    public Flow updateFlow(Flow flow) {
        Flow old = this.findById(flow.getId());
        if (flow.getStatus() == null) {
            flow.setStatus(old.getStatus());
        }
        flow.setUpdateDate(new Date());
        flowMapper.update(flow);
        flow.setCreateDate(old.getCreateDate());
        return flow;
    }

    public void deleteFlow(List<Long> ids) {
        flowMapper.deleteByIds(ids);
    }
}
