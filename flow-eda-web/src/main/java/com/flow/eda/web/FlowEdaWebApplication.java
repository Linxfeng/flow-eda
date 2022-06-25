package com.flow.eda.web;

import com.flow.eda.web.flow.status.FlowInfoServiceImpl;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDubbo
@SpringBootApplication
@EnableTransactionManagement
@DubboComponentScan(basePackageClasses = FlowInfoServiceImpl.class)
@ComponentScan(
        basePackages = {
            "com.flow.eda.web",
            "com.flow.eda.common.config",
            "com.flow.eda.common.resource"
        })
public class FlowEdaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaWebApplication.class, args);
    }
}
