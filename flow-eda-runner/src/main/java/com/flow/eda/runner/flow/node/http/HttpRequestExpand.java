package com.flow.eda.runner.flow.node.http;

import com.flow.eda.common.exception.InvalidParameterException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/** 通过uri和method参数创建一个HttpRequest */
public class HttpRequestExpand extends HttpRequestBase implements HttpEntityEnclosingRequest {
    private static final List<String> METHOD_ALL =
            Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH");
    private final String methodName;
    private HttpEntity entity;

    public HttpRequestExpand(String uri, String method) {
        this.setURI(URI.create(uri));
        this.methodName = checkMethod(method);
    }

    private static String checkMethod(String method) {
        if (method == null) {
            throw new InvalidParameterException("method", "null");
        }
        String m = method.toUpperCase();
        if (!METHOD_ALL.contains(m)) {
            throw new InvalidParameterException("method", m);
        }
        return m;
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
