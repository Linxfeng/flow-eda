package com.flow.eda.runner.node.ws.server;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class WsServerNode extends AbstractNode {
    private String path;
    private List<String> query;
    private String sendAfterConnected;
    private String sendAfterReceived;
    private boolean fanout = false;

    public WsServerNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        setStatus(Status.RUNNING);
        WsServerNodeManager.addEndpoint(this);
        callback.callback(output());
    }

    @Override
    protected void verify(Document params) {
        // 校验路径参数，不支持通配符
        this.path = NodeVerify.requiredUrl(params, "path");
        if (path.contains("{") || path.contains("}")) {
            throw new FlowException("The parameter 'path' can not contains symbols '{' or '}'");
        }

        String q = params.getString("query");
        if (StringUtils.hasLength(q)) {
            this.query = Arrays.asList(q.split(","));
        }

        this.sendAfterConnected = params.getString("sendAfterConnected");
        this.sendAfterReceived = params.getString("sendAfterReceived");

        String isSend = params.getString("fanout");
        if ("Send".equals(isSend)) {
            this.fanout = true;
        }
    }

    public String getPath() {
        return path;
    }

    public List<String> getQuery() {
        return query;
    }

    public String getSendAfterConnected() {
        return sendAfterConnected;
    }

    public String getSendAfterReceived() {
        return sendAfterReceived;
    }

    public boolean isFanout() {
        return fanout;
    }
}
