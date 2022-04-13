package com.flow.eda.runner;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@DubboComponentScan(basePackages = "com.flow.eda.runner.flow.data")
@SpringBootApplication
public class FlowEdaRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaRunnerApplication.class, args);
    }
}
