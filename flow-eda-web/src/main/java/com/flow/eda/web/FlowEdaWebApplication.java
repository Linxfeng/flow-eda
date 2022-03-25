package com.flow.eda.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.flow.eda.web", "com.flow.eda.common.config"})
public class FlowEdaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaWebApplication.class, args);
    }
}
