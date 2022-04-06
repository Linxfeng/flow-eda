package com.flow.eda.runner.flow.node.http;

import com.flow.eda.common.exception.InternalException;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Getter
@Setter
public class HttpNode {
    private String url;
    private String method;
    private String body;
    private Map<String, String> params;
    private Map<String, String> header;

    @SuppressWarnings("unchecked")
    public HttpNode(Map<String, Object> args) {
        this.url = (String) args.get("url");
        this.method = (String) args.get("method");
        this.body = (String) args.get("body");
        this.params = (Map<String, String>) args.get("params");
        this.header = (Map<String, String>) args.get("header");
        this.spliceParamsToUrl();
    }

    public String executeHttpRequest() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpRequestExpand request = new HttpRequestExpand(url, method);
            // 添加请求header
            if (header != null) {
                header.forEach(request::addHeader);
            }
            // 添加请求内容
            if (body != null) {
                request.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(request);
            // 获取结果并返回
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
        return null;
    }

    /** 将请求参数拼接到url上 */
    private void spliceParamsToUrl() {
        if (params == null) {
            return;
        }
        String q = "?";
        String and = "&";
        StringBuilder sb = new StringBuilder(and);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(and);
        }
        sb.deleteCharAt(sb.length() - 1);
        String params = sb.toString();
        if (url.contains(q)) {
            if (url.endsWith(and)) {
                params = params.substring(1);
            }
        } else {
            params = q + params.substring(1);
        }
        this.url = url + params;
    }
}
