package com.flow.eda.runner.node.ws.client;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;

public class WsClientNode extends AbstractNode {
    private String path;
    private String sendAfterConnected;
    private boolean fanout = false;

    public WsClientNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {}

    @Override
    protected void verify(Document params) {
        this.path = NodeVerify.requiredUrl(params, "path");
        this.sendAfterConnected = params.getString("sendAfterConnected");

        String isSend = params.getString("fanout");
        if ("Send".equals(isSend)) {
            this.fanout = true;
        }
    }
}
