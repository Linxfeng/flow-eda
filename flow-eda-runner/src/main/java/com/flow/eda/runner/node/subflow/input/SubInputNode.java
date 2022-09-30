package com.flow.eda.runner.node.subflow.input;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import org.bson.Document;

/** 子输入节点，可接收其他流程中的[子流程节点]的输入参数 */
public class SubInputNode extends AbstractNode {
    private Document subInput;

    public SubInputNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        Document output = output();
        if (subInput != null) {
            output.putAll(subInput);
        }
        setStatus(Status.FINISHED);
        callback.callback(output);
    }

    @Override
    protected void verify(Document params) {
        this.subInput = params.get("subInput", Document.class);
    }
}
