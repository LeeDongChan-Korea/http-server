package com.nhn.web.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import com.nhn.web.server.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 서버 진입 클래스
 * - 요청을 받아서 RequestProcessor에 넘김
 */
public class HttpServer {
    private final ServerConfig config;
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static final int NUM_THREADS = 50;

    public HttpServer(ServerConfig config) {
        this.config = config;
    }

    /**
     * 서버를 시작하고 클라이언트 연결 처리
     */
    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        try (ServerSocket server = new ServerSocket(config.getPort())) {
            logger.info("서버 시작됨 - 포트 {}", server.getLocalPort());

            while (true) {
                try {
                    Socket clientSocket = server.accept();
                    pool.submit(new RequestProcessor(config, clientSocket));
                } catch (IOException e) {
                    logger.error("요청 수락 실패", e);
                }
            }
        } catch (IOException e) {
            logger.error("서버 시작 중 오류 발생", e);
        } finally {
            pool.shutdown();
        }
    }

    /**
     * 서버 실행 메인 메서드
     */
    public static void main(String[] args) {
        try {
            String configPath = args.length > 0 ? args[0] : "config.json";
            ServerConfig config = ServerConfig.loadFromFile(configPath);
            new HttpServer(config).start();
        } catch (Exception e) {
            logger.error("서버 구동 중 오류 발생", e);
        }
    }
}
