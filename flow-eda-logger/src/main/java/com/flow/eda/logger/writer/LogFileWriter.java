package com.flow.eda.logger.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class LogFileWriter {
    private final String path;
    private final DateFormat format = new SimpleDateFormat("yyyy_MM_dd");

    public LogFileWriter() {
        this.path = System.getProperty("user.dir") + "\\logs";
    }

    /** 若指定目录不存在，则创建目录 */
    private static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void write(String path, String message) {
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            fw = new FileWriter(path, true);
            pw = new PrintWriter(fw);
            pw.print(message);
            pw.flush();
            fw.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /** 初始化日志目录 */
    @PostConstruct
    public void initLogsDir() {
        createDir(path);
        createDir(path + "\\running");
        createDir(path + "\\operation");
    }

    /** 写入操作日志文件 */
    public void writeOperationLogs(String message) {
        String date = format.format(new Date());
        write(path + "\\operation\\" + date + ".log", message);
    }

    /** 写入流程运行日志文件 */
    public void writeRunningLogs(String flowId, String message) {
        String dir = path + "\\running\\" + flowId;
        createDir(dir);
        String date = format.format(new Date());
        write(dir + "\\" + date + ".log", message);
    }
}
