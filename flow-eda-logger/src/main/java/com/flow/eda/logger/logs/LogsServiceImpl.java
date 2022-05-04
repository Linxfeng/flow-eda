package com.flow.eda.logger.logs;

import com.flow.eda.common.dubbo.api.LogsService;
import com.flow.eda.common.dubbo.model.Logs;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@DubboService(interfaceClass = LogsService.class)
public class LogsServiceImpl implements LogsService {
    private static final String ROOT = System.getProperty("user.dir");

    @Override
    public List<Logs> getLogList(Type type) {
        if (Type.OPERATION == type) {
            return getOperationLogs();
        } else {
            return getRunningLogs();
        }
    }

    /** 获取操作日志信息列表 */
    private List<Logs> getOperationLogs() {
        List<Logs> result = new ArrayList<>();
        String path = "\\logs\\operation";
        File dir = new File(ROOT + path);
        if (dir.exists()) {
            File[] logs = dir.listFiles();
            if (logs != null) {
                for (File file : logs) {
                    result.add(new Logs(file.getName()).fileInfo(path, file.length()));
                }
            }
        }
        return result;
    }

    /** 获取运行日志信息列表 */
    private List<Logs> getRunningLogs() {
        List<Logs> result = new ArrayList<>();
        String path = "\\logs\\running";
        File dir = new File(ROOT + path);
        if (!dir.exists()) {
            return result;
        }
        File[] flows = dir.listFiles();
        if (flows == null) {
            return result;
        }
        for (File flowDir : flows) {
            File[] files = flowDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    Logs logs = new Logs(file.getName(), flowDir.getName());
                    logs.fileInfo(path, file.length());
                    result.add(logs);
                }
            }
        }
        return result;
    }
}
