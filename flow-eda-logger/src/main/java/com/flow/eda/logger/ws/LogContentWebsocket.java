package com.flow.eda.logger.ws;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 用于向前端推送日志文件内容 */
@Slf4j
@Component
@EqualsAndHashCode
@ServerEndpoint("/ws/logs/content/{path}")
public class LogContentWebsocket {
    /** 每次推送日志行数 */
    private static final int LINES = 20;
    /** 日志文件根目录 */
    private static final String ROOT = System.getProperty("user.dir");
    /** 用于异步推送日志文件内容的线程池 */
    private final ExecutorService pool = Executors.newCachedThreadPool();

    @OnOpen
    public void onOpen(Session session, @PathParam("path") String path) {
        log.info("Connected: new logs content request client connected, {}", path);
        // 客户端建立连接之后立即开始推送
        pool.execute(() -> pushLogContent(path, session));
    }

    @OnClose
    public void onClose(@PathParam("path") String path) {
        log.info("Disconnected: logs content request client disconnected, {}", path);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket session {} onError:{}", session.getId(), error.getMessage());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("Received client message:{}", message);
    }

    /** 推送日志文件内容 */
    private void pushLogContent(String path, Session session) {
        try {
            String filePath = ROOT + path.replaceAll(":", "/");
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            StringBuilder builder = new StringBuilder();
            int lines = 0;
            while (in.ready()) {
                builder.append(in.readLine()).append("\n");
                lines++;
                if (lines == LINES) {
                    this.sendMessage(session, builder.toString());
                    builder = new StringBuilder();
                    lines = 0;
                }
            }
            if (lines > 0) {
                this.sendMessage(session, builder.toString());
            }
            in.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendMessage(Session session, String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("Send websocket message failed:{}", e.getMessage());
            }
        }
    }
}
