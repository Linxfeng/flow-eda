package com.flow.eda.runner.flow.node.start;

import com.flow.eda.runner.flow.node.Node;
import com.flow.eda.runner.flow.node.NodeFunction;

public class StartNode implements Node {

    @Override
    public void run(NodeFunction function) {
        function.callback(null);
    }
}
