package com.flow.eda.runner.node.http.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.eda.common.exception.FlowException;
import com.flow.eda.common.exception.InternalException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestNode extends AbstractNode {
    private String url;
    private String method;
    private String body;
    private List<String[]> headers;

    public HttpRequestNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction function) {
        try {
            Document res = this.executeHttpRequest();
            setStatus(Status.FINISHED);
            function.callback(output().append("httpResult", res));
        } catch (Exception e) {
            throw FlowException.wrap(e);
        }
    }

    @Override
    protected void verify(Document params) {
        try {
            NodeVerify.notNull(params, "url");
            String url = params.getString("url");
            NodeVerify.notBlank(url, "url");
            if (url.startsWith("/")) {
                url = "http://localhost:8088" + url;
            } else {
                NodeVerify.isTrue(url.startsWith("http"), "url");
            }
            this.url = url;
            this.method = NodeVerify.requiredMethod(params);

            String param = params.getString("params");
            if (StringUtils.hasLength(param)) {
                NodeVerify.isTrue(param.contains("=") && !param.startsWith("?"), "params");
                NodeVerify.isTrue(!param.startsWith("=") && !param.endsWith("="), "params");
                this.url = url + "?" + param;
            }

            this.body = params.getString("body");
            if (StringUtils.hasLength(body)) {
                NodeVerify.isTrue(body.startsWith("{") && body.endsWith("}"), "body");
                try {
                    Document.parse(body);
                } catch (Exception ignore) {
                    NodeVerify.throwWithName("body");
                }
            }

            String header = params.getString("header");
            if (StringUtils.hasLength(header)) {
                this.headers = new ArrayList<>();
                for (String h : header.split(",")) {
                    NodeVerify.isTrue(h.contains(":"), "header");
                    String[] str = new String[2];
                    str[0] = h.trim().split(":")[0].trim();
                    NodeVerify.notNull(str[0], "header");
                    str[1] = h.trim().split(":")[1].trim();
                    NodeVerify.notNull(str[1], "header");
                    this.headers.add(str);
                }
            }
        } catch (Exception e) {
            throw FlowException.wrap(e, "The http_request node parameters is invalid");
        }
    }

    private Document executeHttpRequest() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpRequestExpand request = new HttpRequestExpand(url, method);
        final String contentType = "Content-Type";
        // 添加请求header
        if (headers != null) {
            for (String[] header : headers) {
                request.addHeader(header[0], header[1]);
            }
        }
        // 默认返回json格式
        Header[] requestHeaders = request.getHeaders(contentType);
        if (requestHeaders == null || requestHeaders.length == 0) {
            request.addHeader(contentType, "application/json;charset=utf-8");
        }
        // 添加请求内容
        if (body != null) {
            request.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        }
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(request);
        // 获取结果并返回
        if (response != null && response.getEntity() != null) {
            String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            // 根据返回结果类型对应解析
            Header[] headers = response.getHeaders(contentType);
            if (headers != null && headers[0] != null && headers[0].getValue() != null) {
                String value = headers[0].getValue();
                // 处理返回html格式
                if (value.startsWith("text/html")) {
                    return new Document("html", res);
                }
                // 处理返回xml格式
                if (value.startsWith("application/xml")) {
                    return new Document("xml", res);
                }
            }
            // 处理返回json格式
            try {
                return new ObjectMapper().readValue(res, Document.class);
            } catch (Exception ignored) {
                throw new FlowException("The http response result is not in json or html format");
            }
        }
        throw new InternalException("The http request has no response.");
    }
}
