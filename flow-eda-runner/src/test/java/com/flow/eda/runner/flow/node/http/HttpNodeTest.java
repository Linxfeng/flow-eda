package com.flow.eda.runner.flow.node.http;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HttpNodeTest {

    @Test
    void executeHttpRequest() {
        Document args = new Document();
        args.append("url", "http://localhost:8081/api/v1/node/type");
        args.append("method", "get");
        args.append("params", new Document("limit", "3"));
        args.append("header", new Document("Accept", "application/json"));
        HttpNode httpNode = new HttpNode(args);
        httpNode.run(Assertions::assertNotNull);
    }
}
