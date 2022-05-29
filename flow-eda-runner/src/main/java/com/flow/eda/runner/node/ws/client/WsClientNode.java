package com.flow.eda.runner.node.ws.client;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import lombok.Getter;
import org.bson.Document;
import org.springframework.util.StringUtils;

/** WebSocket客户端节点 */
@Getter
public class WsClientNode extends AbstractNode {
    private String path;
    private String query;
    private String sendAfterConnect;
    private NodeFunction callback;

    public WsClientNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        // 同一节点不可重复运行
        if (WsClientNodeManager.contains(getNodeId())) {
            throw new FlowException("The ws_client node can not run repeatedly");
        }
        setStatus(Status.RUNNING);
        this.callback = callback;

        // 创建ws客户端连接
        WsClientNodeManager.createWebSocketClient(this);
    }

    @Override
    protected void verify(Document params) {
        String uri = NodeVerify.requiredUrl(params, "path");
        if (uri.contains("?")) {
            this.path = uri.split("\\?")[0];
            String q = uri.split("\\?")[1];
            if (StringUtils.hasText(q) && q.contains("=")) {
                this.query = q;
            }
        } else {
            this.path = uri;
        }

        this.sendAfterConnect = params.getString("sendAfterConnect");
    }

    /** 回调，收到消息后向下游节点输出 */
    public void callback(String message) {
        this.callback.callback(output().append("message", message));
    }

    public String getUri() {
        if (query != null) {
            return path + "?" + query;
        }
        return path;
    }
}
