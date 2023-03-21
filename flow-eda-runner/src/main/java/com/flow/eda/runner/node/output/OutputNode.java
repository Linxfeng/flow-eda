package com.flow.eda.runner.node.output;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.utils.NodeParamsUtil;
import org.bson.Document;

public class OutputNode extends AbstractNode {
    private final NodeParamsUtil nodeParams;

    public OutputNode(Document params) {
        super(params);
        this.nodeParams = new NodeParamsUtil(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.FINISHED);
        callback.callback(this.nodeParams.output(this).get());
    }

    @Override
    protected void verify(Document params) {}
}
