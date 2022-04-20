package com.flow.eda.runner.flow.node.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.eda.common.exception.InternalException;
import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class HttpNode extends AbstractNode {
    private String url;
    private String method;
    private String body;
    private String params;
    private String header;

    public HttpNode(Document params) {
        super(params);
        this.url = params.getString("url");
        this.method = params.getString("method");
        this.body = params.getString("body");
        this.params = params.getString("params");
        this.header = params.getString("header");
        if (this.params != null) {
            this.url = this.url + "?" + this.params;
        }
    }

    @Override
    public void run(NodeFunction function) {
        try {
            System.out.println("执行HTTP节点！");
            Document res = this.executeHttpRequest();
            setStatus(Status.FINISHED);
            function.callback(output().append("httpResult", res));
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    private Document executeHttpRequest() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpRequestExpand request = new HttpRequestExpand(url, method);
        // 添加请求header
        if (header != null) {
            for (String h : header.split(",")) {
                request.addHeader(h.split(":")[0].trim(), h.split(":")[1].trim());
            }
        }
        // 添加请求内容
        if (body != null) {
            request.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        }
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(request);
        // 获取结果并返回
        if (response != null) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return new ObjectMapper().readValue(res, Document.class);
            }
            throw new Exception(
                    "The http request failed.status code "
                            + response.getStatusLine().getStatusCode());
        }
        throw new Exception("The http request has no response.");
    }
}
