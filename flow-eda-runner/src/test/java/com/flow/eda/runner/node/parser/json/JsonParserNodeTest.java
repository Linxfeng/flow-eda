package com.flow.eda.runner.node.parser.json;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class JsonParserNodeTest {

    @Test
    void run() {
        Document params = new Document();
        List<Document> result = new ArrayList<>();
        Document res = new Document("type", "start");
        result.add(res);
        params.append("httpResult", new Document("result", result));
        params.append("input", new Document("a", "xxx"));
        params.append("parseKey", "httpResult.result.$0.type, params.a");
        JsonParserNode node = new JsonParserNode(params);
        node.run(
                (p) ->
                        Assertions.assertTrue(
                                p.containsKey("httpResult.result.$0.type")
                                        && p.containsKey("params")));
    }
}
