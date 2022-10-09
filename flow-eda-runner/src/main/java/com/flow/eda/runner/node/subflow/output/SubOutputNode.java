package com.flow.eda.runner.node.subflow.output;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.subflow.SubFlowRuntime;
import org.bson.Document;

import java.util.Optional;

/** 子输出节点，可将本节点的输出参数传递至其他流程中的[子流程节点] */
public class SubOutputNode extends AbstractNode {
    private final Document output;

    public SubOutputNode(Document params) {
        super(params);
        this.output = Optional.ofNullable(params).orElseGet(Document::new);
    }

    @Override
    public void run(NodeFunction callback) {
        // 子流程输出
        output.putAll(output());
        SubFlowRuntime.setSubOutput(getFlowId(), output);

        setStatus(Status.FINISHED);
        callback.callback(output);
    }

    @Override
    protected void verify(Document params) {}
}
