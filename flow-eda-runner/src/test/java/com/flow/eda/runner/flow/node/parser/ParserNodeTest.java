package com.flow.eda.runner.flow.node.parser;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ParserNodeTest {

    @Test
    void run() {
        Document params = new Document();
        List<Document> result = new ArrayList<>();
        Document res = new Document("type", "start");
        result.add(res);
        params.append("httpResult", new Document("result", result));
        params.append("input", new Document("a", "xxx"));
        params.append("parseKey", "httpResult.result.$0.type, params.a");
        ParserNode parserNode = new ParserNode(params);
        parserNode.run(
                (p) ->
                        Assertions.assertTrue(
                                p.containsKey("httpResult.result.$0.type")
                                        & p.containsKey("params.a")));
    }
}
