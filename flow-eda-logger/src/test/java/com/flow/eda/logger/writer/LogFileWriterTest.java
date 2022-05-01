package com.flow.eda.logger.writer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LogFileWriterTest {
    @Autowired private LogFileWriter logFileWriter;

    @Test
    void writeOperationLogs() {
        logFileWriter.writeOperationLogs("test message");
    }
}
