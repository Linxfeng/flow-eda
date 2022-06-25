package com.flow.eda.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.flow.eda.oauth2", "com.flow.eda.common.config"})
public class FlowEdaOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaOauth2Application.class, args);
    }
}
