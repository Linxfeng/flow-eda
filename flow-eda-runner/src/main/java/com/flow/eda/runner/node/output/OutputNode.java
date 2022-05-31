package com.flow.eda.runner.node.output;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import org.bson.Document;

import java.util.Optional;

public class OutputNode extends AbstractNode {
    private final Document output;

    public OutputNode(Document params) {
        super(params);
        this.output = Optional.ofNullable(params).orElseGet(Document::new);
    }

    @Override
    public void run(NodeFunction callback) {
        Document input = getInput();
        if (!input.isEmpty()) {
            output.append("params", input);
        }
        output.putAll(output());
        setStatus(Status.FINISHED);
        callback.callback(output);
    }

    @Override
    protected void verify(Document params) {}
}
