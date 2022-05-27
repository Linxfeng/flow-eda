package com.flow.eda.runner.node.ws.server;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
@Getter
public class WsServerNode extends AbstractNode {
    private String path;
    private String message;

    public WsServerNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.RUNNING);
        callback.callback(output());
    }

    @Override
    protected void verify(Document params) {
        this.path = params.getString("path");
        this.message = params.getString("message");
    }
}
