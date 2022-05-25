package com.flow.eda.runner.node.http.response;

import org.bson.Document;
import org.junit.jupiter.api.Test;

class HttpResponseNodeTest {

    @Test
    void run() {
        Document args = new Document();
        args.append("uri", "/api/v1/test");
        args.append("method", "GET");
        args.append("resData", "{'result': 'OK'}");
        HttpResponseNode httpResponseNode = new HttpResponseNode(args);
        httpResponseNode.run(p -> {});
    }
}
