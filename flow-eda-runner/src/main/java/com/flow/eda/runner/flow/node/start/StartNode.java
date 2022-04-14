package com.flow.eda.runner.flow.node.start;

import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

/** 开始节点，用于标志流程的执行起始节点 */
public class StartNode extends AbstractNode {

    public StartNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        System.out.println("执行开始节点！");
        function.callback(output());
    }
}
