package com.flow.eda.oauth2;

import com.flow.eda.oauth2.config.Oauth2ScopeServiceImpl;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
@SpringBootApplication
@DubboComponentScan(basePackageClasses = Oauth2ScopeServiceImpl.class)
@ComponentScan(basePackages = {"com.flow.eda.oauth2", "com.flow.eda.common.config"})
public class FlowEdaOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(FlowEdaOauth2Application.class, args);
    }
}
