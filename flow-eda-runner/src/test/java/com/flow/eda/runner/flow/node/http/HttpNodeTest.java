package com.flow.eda.runner.flow.node.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class HttpNodeTest {

    @Test
    void executeHttpRequest() {
        Map<String, Object> args = new HashMap<>();
        args.put("url", "http://localhost:8081/api/v1/node/type");
        args.put("method", "get");
        Map<String, String> params = new HashMap<>();
        params.put("limit", "3");
        args.put("params", params);
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        args.put("header", header);
        HttpNode httpNode = new HttpNode(args);
        String response = httpNode.executeHttpRequest();
        Assertions.assertNotNull(response);
    }
}
