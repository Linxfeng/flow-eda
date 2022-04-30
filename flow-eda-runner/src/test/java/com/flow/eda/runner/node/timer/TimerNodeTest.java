package com.flow.eda.runner.node.timer;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimerNodeTest {

    @Test
    void run() {
        Document params = new Document("period", "1,SECONDS").append("times", "3");
        TimerNode timerNode = new TimerNode(params);
        final int[] n = {0};
        timerNode.run((p) -> n[0]++);
        try {
            // 主线程等待执行完毕
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
        Assertions.assertEquals(n[0], 3);
    }
}
