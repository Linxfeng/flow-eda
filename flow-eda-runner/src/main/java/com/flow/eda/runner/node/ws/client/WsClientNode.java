package com.flow.eda.runner.node.ws.client;

import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import lombok.Setter;
import org.bson.Document;

/** WebSocket客户端节点 */
@Setter
public class WsClientNode extends AbstractNode {
    private String path;
    private String query;
    private String sendAfterConnect;
    private boolean fanout = false;
    private NodeFunction callback;

    public WsClientNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.RUNNING);
        this.callback = callback;

        // 上游节点输出参数发送

    }

    @Override
    protected void verify(Document params) {
        String uri = NodeVerify.requiredUrl(params, "path");

        if (uri.contains("?")) {
            this.path = uri.split("\\?")[0];
            this.query = uri.split("\\?")[1];
        } else {
            this.path = uri;
        }

        this.sendAfterConnect = params.getString("sendAfterConnect");

        String isSend = params.getString("fanout");
        if ("Send".equals(isSend)) {
            this.fanout = true;
        }
    }
}
