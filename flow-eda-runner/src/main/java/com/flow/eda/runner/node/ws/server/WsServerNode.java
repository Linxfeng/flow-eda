package com.flow.eda.runner.node.ws.server;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
@Getter
public class WsServerNode extends AbstractNode {
    private String path;
    private String sendAfterConnected;
    private String sendAfterReceived;
    private boolean fanout = false;

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
        this.path = NodeVerify.requiredUrl(params, "path");
        this.sendAfterConnected = params.getString("sendAfterConnected");
        this.sendAfterReceived = params.getString("sendAfterReceived");

        String isSend = params.getString("fanout");
        if ("Send".equals(isSend)) {
            this.fanout = true;
        }
    }
}
