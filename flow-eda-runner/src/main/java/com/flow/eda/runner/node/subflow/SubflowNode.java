package com.flow.eda.runner.node.subflow;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;

/** 子流程节点，运行此节点相当于运行子流程 */
public class SubflowNode extends AbstractNode {
    /** 子流程的id */
    private String subflow;

    public SubflowNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        System.out.println(subflow);
    }

    @Override
    protected void verify(Document params) {
        this.subflow = params.getString("subflow");
        NodeVerify.notNull(subflow, "subflow");
    }
}
