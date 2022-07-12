package com.flow.eda.web.log;

import com.flow.eda.common.http.Result;
import com.flow.eda.common.model.Logs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/** 日志服务接口 */
@FeignClient(name = "log", url = "localhost:8082")
public interface LogClient {
    /**
     * 获取日志信息列表
     *
     * @param type 日志类型
     * @return 日志信息列表
     */
    @GetMapping("/api/v1/feign/logs")
    Result<List<Logs>> getLogList(@RequestParam String type);

    /**
     * 删除日志文件
     *
     * @param path 日志文件路径
     */
    @DeleteMapping("/api/v1/feign/logs")
    void deleteLogFiles(@RequestBody List<String> path);
}
