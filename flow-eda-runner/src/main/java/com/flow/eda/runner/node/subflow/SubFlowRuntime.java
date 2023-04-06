package com.flow.eda.runner.node.subflow;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.model.FlowData;
import com.flow.eda.runner.node.NodeTypeEnum;
import com.flow.eda.runner.runtime.FlowDataRuntime;
import com.flow.eda.runner.status.FlowStatusService;
import com.flow.eda.runner.utils.ApplicationContextUtil;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** 子流程运行时 */
public class SubFlowRuntime {
    /** 子流程运行状态：运行中true，运行完成false */
    public static final Map<String, Boolean> SUB_FLOW_MAP = new ConcurrentHashMap<>();
    /** 子流程的输出 */
    public static final Map<String, Document> SUB_OUTPUT = new ConcurrentHashMap<>();

    /** 开始运行子流程 */
    public static void startRunSubFlow(String flowId, Document input) {
        try {
            // 获取子流程数据
            FlowStatusService service = ApplicationContextUtil.getBean(FlowStatusService.class);
            List<FlowData> flowData = service.getFlowData(flowId);

            // 将输入参数设置到[子输入]节点中
            if (input != null && !input.isEmpty()) {
                setSubInput(flowData, input);
            }

            // 注册通知服务，当流程运行完成后会发起通知，更新SUB_FLOW_MAP状态
            SUB_FLOW_MAP.put(flowId, true);
            service.registerNotice(flowId, (id) -> SUB_FLOW_MAP.put(id, false));

            // 运行子流程
            FlowDataRuntime runtime = ApplicationContextUtil.getBean(FlowDataRuntime.class);
            runtime.runFlowData(flowData);
        } catch (Exception e) {
            throw new FlowException(e.getMessage());
        }
    }

    /** 设置子流程的输入参数 */
    private static void setSubInput(List<FlowData> list, Document input) {
        list.stream()
                .filter(f -> NodeTypeEnum.SUB_INPUT.getType().equals(f.getType()))
                .forEach(
                        f -> {
                            Document params =
                                    Optional.ofNullable(f.getParams()).orElse(new Document());
                            params.append("subInput", input);
                            f.setParams(params);
                        });
    }

    /** 设置子流程的输出参数 */
    public static void setSubOutput(String flowId, Document output) {
        if (SUB_FLOW_MAP.containsKey(flowId)) {
            SUB_OUTPUT.put(flowId, output);
        }
    }
}
