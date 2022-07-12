package com.flow.eda.runner.node.splice;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class SpliceNodeTest {

    @Test
    void run() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document params =
                    new Document("field", "index")
                            .append("nodeId", "node_xxx")
                            .append("flowId", "flow_xxx")
                            .append("index", i + 1);
            SpliceNode node = new SpliceNode(params);
            node.run(p -> result.add(p.getString("result")));
        }
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("1,2,3,4,5,6,7,8,9,10", result.get(0));
    }
}
