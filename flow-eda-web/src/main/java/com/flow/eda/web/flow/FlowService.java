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
        flow.setStatus(Flow.Status.INIT);
        flow.setCreateDate(new Date());
        flow.setUpdateDate(new Date());
        flowMapper.insert(flow);
    }

    public Flow findById(String id) {
        Flow flow = flowMapper.findById(id);
        if (flow == null) {
            throw new ResourceNotFoundException("id", id);
        }
        return flow;
    }

    public Flow updateFlow(Flow flow) {
        flow.setUpdateDate(new Date());
        flowMapper.update(flow);
        return findById(flow.getId());
    }

    public void deleteFlow(List<String> ids) {
        flowMapper.deleteByIds(ids);
    }
}
