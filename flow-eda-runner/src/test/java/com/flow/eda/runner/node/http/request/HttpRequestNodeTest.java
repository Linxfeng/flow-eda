package com.flow.eda.runner.node.http.request;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HttpRequestNodeTest {

    @Test
    void run() {
        Document args = new Document();
        args.append("url", "http://localhost:8081/api/v1/node/type");
        args.append("method", "GET");
        args.append("header", "Accept:application/json");
        HttpRequestNode httpRequestNode = new HttpRequestNode(args);
        httpRequestNode.run(p -> Assertions.assertTrue(p.containsKey("httpResult")));
    }
}
