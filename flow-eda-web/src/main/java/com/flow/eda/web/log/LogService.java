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
    public List<Logs> getLogList(LogRequest request, String username) {
        List<Logs> list = logsService.getLogList(request.getType());
        List<String> ids;
        if (username != null) {
            // 根据用户进行过滤
            List<String> idsByUser = flowMapper.findIdsByUser(username);
            ids =
                    filterMap(
                            list,
                            l -> l.getFlow() != null && idsByUser.contains(l.getFlow()),
                            Logs::getFlow);
        } else {
            ids = filterMap(list, l -> l.getFlow() != null, Logs::getFlow);
        }
        if (CollectionUtil.isNotEmpty(ids)) {
            list.removeIf(l -> !ids.contains(l.getFlow()));
            // 聚合流程名称
            List<Flow> flows = flowMapper.findByIds(ids);
            list =
                    MergeBuilder.source(list, Logs::getFlow)
                            .target(flows, Flow::getId)
                            .mergeS((log, flow) -> log.setFlowName(flow.getName()));
        } else if (LogsService.Type.RUNNING.equals(request.getType())) {
            list.clear();
        }
        // 根据日志的日期排序，降序
        list.sort((o1, o2) -> comparingDate(o1.getDate(), o2.getDate()));
        return list;
    }

    /** 删除日志文件 */
    public void deleteLogs(List<String> path) {
        logsService.deleteLogFiles(path);
    }

    private int comparingDate(String date1, String date2) {
        String[] split1 = date1.split("-");
        String[] split2 = date2.split("-");
        if (Integer.parseInt(split1[0]) == Integer.parseInt(split2[0])) {
            int i1 = Integer.parseInt(split1[1] + split1[2]);
            int i2 = Integer.parseInt(split2[1] + split2[2]);
            if (i1 == i2) {
                return 0;
            }
            return i1 > i2 ? -1 : 1;
        }
        return Integer.parseInt(split1[0]) > Integer.parseInt(split2[0]) ? -1 : 1;
    }
}
