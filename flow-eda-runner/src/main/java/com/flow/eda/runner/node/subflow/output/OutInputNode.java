package com.flow.eda.runner.node.subflow.output;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import org.bson.Document;

/** 子输出节点，可将本节点的输出参数传递至其他流程中的[子流程节点] */
public class OutInputNode extends AbstractNode {

    public OutInputNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.FINISHED);
        callback.callback(output());
    }

    @Override
    protected void verify(Document params) {}
}
