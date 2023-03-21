package com.flow.eda.runner.utils;

import com.flow.eda.runner.node.AbstractNode;
import org.bson.Document;

/** 节点参数工具 */
public class NodeParamsUtil {

    private final Document params;

    public NodeParamsUtil(Document p) {
        Document params = new Document();
        params.putAll(p);
        this.params = params;
    }

    public NodeParamsUtil remove(String... keys) {
        for (String key : keys) {
            this.params.remove(key);
        }
        return this;
    }

    public NodeParamsUtil output(AbstractNode node) {
        Document input = node.getInput();
        if (!input.isEmpty()) {
            this.params.append("params", input);
        }
        this.params.putAll(node.output());
        return this;
    }

    public Document get() {
        return this.params;
    }
}
