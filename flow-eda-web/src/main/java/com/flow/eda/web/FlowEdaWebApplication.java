package com.flow.eda.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableScheduling
@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
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
