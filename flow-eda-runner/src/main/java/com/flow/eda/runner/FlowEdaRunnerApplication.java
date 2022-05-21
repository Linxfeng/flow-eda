package com.flow.eda.runner;

import com.flow.eda.runner.status.FlowNodeWebsocket;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableDubbo
@DubboComponentScan(basePackages = "com.flow.eda.runner.data")
@SpringBootApplication
public class FlowEdaRunnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(FlowEdaRunnerApplication.class, args);
        FlowNodeWebsocket.setApplicationContext(applicationContext);
    }
}
