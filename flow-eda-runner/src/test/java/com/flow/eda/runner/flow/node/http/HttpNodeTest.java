package com.flow.eda.runner.flow.node.http;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HttpNodeTest {

    @Test
    void run() {
        Document args = new Document();
        args.append("url", "http://localhost:8081/api/v1/node/type");
        args.append("method", "get");
        args.append("header", "Accept:application/json");
        HttpNode httpNode = new HttpNode(args);
        httpNode.run(p -> Assertions.assertTrue(p.containsKey("httpResult")));
    }
}
