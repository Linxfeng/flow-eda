package com.flow.eda.runner.flow.node.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flow.eda.common.exception.InternalException;
import com.flow.eda.common.exception.InvalidParameterException;
import com.flow.eda.runner.flow.node.AbstractNode;
import com.flow.eda.runner.flow.node.NodeFunction;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.flow.eda.runner.flow.node.NodeVerify.*;

public class HttpNode extends AbstractNode {
    private String url;
    private String method;
    private String body;
    private List<String[]> headers;

    public HttpNode(Document params) {
        super(params);
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

    @Override
    protected void verify(Document params) {
        try {
            notNull(params, "url");
            this.url = params.getString("url");
            notBlank(url, "url");

            this.method = params.getString("method");
            notBlank(method, "method");
            List<String> list =
                    Arrays.asList(
                            "GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH");
            isTrue(list.contains(method), "method");

            String param = params.getString("params");
            if (StringUtils.hasLength(param)) {
                isTrue(param.contains("=") && !param.startsWith("?"), "params");
                isTrue(!param.startsWith("=") && !param.endsWith("="), "params");
                this.url = url + "?" + param;
            }

            this.body = params.getString("body");
            if (StringUtils.hasLength(body)) {
                isTrue(body.startsWith("{") && body.endsWith("}"), "body");
            }

            String header = params.getString("header");
            if (StringUtils.hasLength(header)) {
                this.headers = new ArrayList<>();
                for (String h : header.split(",")) {
                    isTrue(h.contains(":"), "header");
                    String[] str = new String[2];
                    str[0] = h.split(":")[0].trim();
                    notNull(str[0], "header");
                    str[1] = h.split(":")[1].trim();
                    notNull(str[1], "header");
                    this.headers.add(str);
                }
            }
        } catch (InvalidParameterException e) {
            throw e;
        } catch (Exception ignore) {
            throw new InvalidParameterException("The http node parameters is invalid");
        }
    }

    private Document executeHttpRequest() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpRequestExpand request = new HttpRequestExpand(url, method);
        // 添加请求header
        if (headers != null) {
            for (String[] header : headers) {
                request.addHeader(header[0], header[1]);
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
