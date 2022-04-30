package com.flow.eda.runner.node.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

/** 通过uri和method参数创建一个HttpRequest */
public class HttpRequestExpand extends HttpRequestBase implements HttpEntityEnclosingRequest {
    private final String methodName;
    private HttpEntity entity;

    public HttpRequestExpand(String uri, String method) {
        this.setURI(URI.create(uri));
        this.methodName = method;
    }

    @Override
    public String getMethod() {
        return this.methodName;
    }

    @Override
    public boolean expectContinue() {
        Header expect = this.getFirstHeader("Expect");
        return expect != null && "100-continue".equalsIgnoreCase(expect.getValue());
    }

    @Override
    public HttpEntity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }
}
