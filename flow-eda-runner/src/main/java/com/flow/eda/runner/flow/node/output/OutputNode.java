package com.flow.eda.runner.flow.node.output;

import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.bson.Document;

public class OutputNode extends AbstractNode {

    public OutputNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        System.out.println("执行输出节点！输出：");
        System.out.println(getInput().toJson());
        callback.callback(output());
    }
}
