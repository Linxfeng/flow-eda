package com.flow.eda.runner.node.split;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SplitNodeTest {

    @Test
    void run() {
        List<String> result = new ArrayList<>();
        Document params =
                new Document("field", "index")
                        .append("index", "1,2,3,4,5,6,7,8,9,10")
                        .append("outputWay", "多次输出单值");
        SplitNode node = new SplitNode(params);
        node.run(p -> result.add(p.getString("result")));
        Assertions.assertEquals(10, result.size());
        Assertions.assertEquals("6", result.get(5));
    }
}
