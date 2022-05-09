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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    /** 每个日志文件路径对应一个session */
    private static final Map<String, Session> SESSION_POOL = new ConcurrentHashMap<>();
    /** 用于异步推送日志文件内容的线程池 */
    private final ExecutorService pool = Executors.newCachedThreadPool();

    @OnOpen
    public void onOpen(Session session, @PathParam("path") String path) {
        SESSION_POOL.put(path, session);
        log.info("Connected: new logs content request client connected, {}", path);
        // 客户端建立连接之后立即开始推送
        pool.execute(() -> pushLogContent(path));
    }

    @OnClose
    public void onClose(@PathParam("path") String path) {
        SESSION_POOL.remove(path);
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
    private void pushLogContent(String path) {
        try {
            String filePath = ROOT + path.replaceAll(":", "/");
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            StringBuilder builder = new StringBuilder();
            int lines = 0;
            while (in.ready()) {
                builder.append(in.readLine()).append("\n");
                lines++;
                if (lines == LINES) {
                    sendMessage(path, builder.toString());
                    builder = new StringBuilder();
                    lines = 0;
                }
            }
            if (lines > 0) {
                sendMessage(path, builder.toString());
            }
            in.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendMessage(String path, String message) {
        if (SESSION_POOL.get(path) != null) {
            try {
                synchronized (SESSION_POOL.get(path)) {
                    SESSION_POOL.get(path).getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                log.error("Send websocket message failed:{}", e.getMessage());
            }
        }
    }
}
