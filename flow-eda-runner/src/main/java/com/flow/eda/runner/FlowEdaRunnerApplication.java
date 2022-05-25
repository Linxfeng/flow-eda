package com.flow.eda.runner;

import com.flow.eda.runner.node.http.response.HttpResponseServlet;
import com.flow.eda.runner.status.FlowNodeWebsocket;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@EnableDubbo
@DubboComponentScan(basePackages = "com.flow.eda.runner.data")
@SpringBootApplication
public class FlowEdaRunnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(FlowEdaRunnerApplication.class, args);
        FlowNodeWebsocket.setApplicationContext(applicationContext);
    }

    @Bean
    @Qualifier(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new HttpResponseServlet();
    }
}
