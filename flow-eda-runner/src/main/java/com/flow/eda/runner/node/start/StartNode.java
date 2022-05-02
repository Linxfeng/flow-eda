package com.flow.eda.runner.node.start;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import org.bson.Document;

/** 开始节点，用于标志流程的执行起始节点 */
public class StartNode extends AbstractNode {

    public StartNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        setStatus(Status.FINISHED);
        function.callback(output());
    }

    @Override
    protected void verify(Document params) {}
}
