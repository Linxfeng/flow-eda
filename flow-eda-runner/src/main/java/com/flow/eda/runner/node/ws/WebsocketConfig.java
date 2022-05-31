package com.flow.eda.runner.node.ws;

import com.flow.eda.runner.node.ws.server.WsHandshakeInterceptor;
import com.flow.eda.runner.node.ws.server.WsServerNodeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/** Websocket配置 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /** 注册ws处理器和拦截器，默认path全匹配，由自定义拦截器过滤非法请求 */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WsServerNodeHandler(), "/*")
                .addInterceptors(new WsHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}
