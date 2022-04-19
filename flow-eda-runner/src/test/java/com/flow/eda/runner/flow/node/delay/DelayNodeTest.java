package com.flow.eda.runner.flow.node.delay;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class DelayNodeTest {

    @Test
    void run() {
        Document params = new Document("delay", "1,SECONDS");
        DelayNode delayNode = new DelayNode(params);
        long t1 = Instant.now().toEpochMilli();
        // 延时1秒，误差控制在20ms内
        delayNode.run(
                (p) -> {
                    long t = Instant.now().toEpochMilli() - t1;
                    Assertions.assertTrue(t - 1000 < 20);
                });
    }
}
