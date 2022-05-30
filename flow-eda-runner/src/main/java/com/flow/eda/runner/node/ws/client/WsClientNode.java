package com.flow.eda.runner.node.ws.client;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import com.flow.eda.runner.runtime.FlowBlockNodePool;
import lombok.Getter;
import org.bson.Document;

/** WebSocket客户端节点 */
@Getter
public class WsClientNode extends AbstractNode implements FlowBlockNodePool.BlockNode {
    private String path;
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
        String uri = params.getString("path");
        NodeVerify.notBlank(uri, "path");

        // 兼容自定义ws路径
        if (uri.startsWith("ws")) {
            this.path = uri;
        } else {
            this.path = NodeVerify.requiredUrl(params, "path");
        }

        this.sendAfterConnect = params.getString("sendAfterConnect");
    }

    /** 回调，收到消息后向下游节点输出 */
    public void callback(String message) {
        this.callback.callback(output().append("message", message));
    }

    public String getUri() {
        if (path.startsWith("/")) {
            return "ws://localhost:8088" + path;
        }
        return path;
    }

    @Override
    public void destroy() {
        // 关闭ws连接
        WsClientNodeManager.closeWebSocketClient(this);
    }
}
