package com.flow.eda.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.flow.eda.logger", "com.flow.eda.common.config"})
public class FlowEdaLoggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaLoggerApplication.class, args);
    }

    /** websocket config */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
