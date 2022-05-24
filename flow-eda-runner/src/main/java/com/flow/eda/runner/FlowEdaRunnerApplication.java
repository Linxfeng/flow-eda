package com.flow.eda.runner;

import com.flow.eda.runner.status.FlowNodeWebsocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.flow.eda.runner", "com.flow.eda.common.config"})
public class FlowEdaRunnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(FlowEdaRunnerApplication.class, args);
        FlowNodeWebsocket.setApplicationContext(applicationContext);
    }
}
