package com.flow.eda.web.log;

import com.flow.eda.common.dubbo.api.LogsService;
import com.flow.eda.common.dubbo.model.Logs;
import com.flow.eda.common.utils.CollectionUtil;
import com.flow.eda.common.utils.MergeBuilder;
import com.flow.eda.web.flow.Flow;
import com.flow.eda.web.flow.FlowMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.flow.eda.common.utils.CollectionUtil.filterMap;

@Service
public class LogService {
    @DubboReference private LogsService logsService;
    @Autowired private FlowMapper flowMapper;

    /** 获取日志文件信息列表 */
    public List<Logs> getLogList(LogRequest request) {
        List<Logs> list = logsService.getLogList(request.getType());
        List<String> ids = filterMap(list, l -> l.getFlow() != null, Logs::getFlow);
        if (CollectionUtil.isNotEmpty(ids)) {
            // 聚合流程名称
            List<Flow> flows = flowMapper.findByIds(ids);
            return MergeBuilder.source(list, Logs::getFlow)
                    .target(flows, Flow::getId)
                    .mergeS((log, flow) -> log.setFlowName(flow.getName()));
        }
        return list;
    }

    /** 删除日志文件 */
    public void deleteLogs(List<String> path) {
        logsService.deleteLogFiles(path);
    }
}
