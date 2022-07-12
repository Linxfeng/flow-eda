package com.flow.eda.runner.node.sequence;

import com.flow.eda.runner.node.Node;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SequenceNodeTest {

    @Test
    void run() {
        Document params =
                new Document("start", "6")
                        .append("end", "13")
                        .append("step", "2")
                        .append("action", "递增");
        Node node = new SequenceNode(params);
        node.run(
                p -> {
                    int index = p.getInteger("index");
                    Assertions.assertTrue(index >= 6 && index <= 13);
                    if (index == 12) {
                        Assertions.assertEquals(node.status(), Node.Status.FINISHED);
                    } else {
                        Assertions.assertEquals(node.status(), Node.Status.RUNNING);
                    }
                });
    }
}
