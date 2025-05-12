package com.nhn.web.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nhn.web.server.config.ServerConfig;

/**
 * HTTP 서버의 진입점 클래스.
 */
public class HttpServer {
    private final ServerConfig config;
    private static final Logger logger = Logger.getLogger(HttpServer.class.getCanonicalName());
    private static final int NUM_THREADS = 50;

    public HttpServer(ServerConfig config) {
        this.config = config;
    }

    /**
     * 서버 실행 메서드
     */
    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        try (ServerSocket server = new ServerSocket(config.getPort())) {
            logger.info("Accepting connections on port " + server.getLocalPort());

            while (true) {
                try {
                    Socket request = server.accept();
                    pool.submit(new RequestProcessor(config, request));
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Failed to accept connection", e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server failed to start", e);
        } finally {
            pool.shutdown(); // 정상 종료 시 리소스 정리
        }
    }

    /**
     * 서버 시작 진입점
     */
    public static void main(String[] args) {
        try {
            String configPath = args.length > 0 ? args[0] : "config.json";
            ServerConfig config = ServerConfig.loadFromFile(configPath);
            new HttpServer(config).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
