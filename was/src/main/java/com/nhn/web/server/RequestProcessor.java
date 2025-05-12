package com.nhn.web.server;

import com.nhn.web.server.config.HttpStatus;
import com.nhn.web.server.config.ServerConfig;
import com.nhn.web.server.servlet.ServletMapper;
import com.nhn.web.server.servlet.SimpleServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 클라이언트 요청 1건을 처리하는 쓰레드 단위 작업 클래스
 */
public class RequestProcessor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
    private final ServerConfig config;
    private final Socket connection;
    private final ErrorHandler errorHandler = new ErrorHandler();

    public RequestProcessor(ServerConfig config, Socket connection) {
        this.config = config;
        this.connection = connection;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            OutputStream out = connection.getOutputStream()
        ) {
            HttpRequest req;
            String host;

            // 요청 파싱
            try {
                req = RequestParser.parseRequest(in);
                host = req != null ? req.getHost() : null;
            } catch (UnsupportedOperationException e) {
                sendErrorPage(out, HttpStatus.NOT_IMPLEMENTED, null);
                logger.warn("지원하지 않는 요청 형식", e);
                return;
            }

            // 호스트 포트 제거
            if (host != null && host.contains(":")) {
                host = host.split(":")[0];
            }

            // 잘못된 호스트거나 설정에 없음
            if (req == null || host == null || !config.getHosts().containsKey(host)) {
                sendErrorPage(out, HttpStatus.NOT_FOUND, host);
                return;
            }

            // 금지된 경로 체크
            if (SecurityManager.isForbidden(req.getPath())) {
                sendErrorPage(out, HttpStatus.FORBIDDEN, host);
                return;
            }

            String path = req.getPath();
            ServerConfig.HostConfig hostConfig = config.getHosts().get(host);

            // 루트 또는 디렉토리 요청 시 index.html 붙임
            if (path.equals("/") || path.endsWith("/")) {
                path += hostConfig.getIndexFile();
            }

            /* 정적 리소스 처리 */
            String fullPath = hostConfig.getHttpRoot() + path;
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(fullPath);
            HttpResponse res = new HttpResponse(out);

            if (resourceStream != null) {
                res.sendFile(resourceStream, "text/html; charset=utf-8");
                res.flush();
                return;
            }

            /* 서블릿 처리 */
            String className = ServletMapper.mapUrlToClassName(path, config, host);
            if (className == null) {
                sendErrorPage(out, HttpStatus.NOT_FOUND, host);
                logger.warn("매핑되지 않은 경로: {}", path);
                return;
            }

            try {
                Class<?> clazz = Class.forName(className);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                if (!(instance instanceof SimpleServlet)) {
                    throw new IllegalArgumentException("서블릿이 아님");
                }

                ((SimpleServlet) instance).service(req, res);
                res.flush();

            } catch (ClassNotFoundException e) {
                sendErrorPage(out, HttpStatus.NOT_FOUND, host);
                logger.warn("서블릿 클래스 없음: {}", className);
            } catch (IllegalArgumentException e) {
                sendErrorPage(out, HttpStatus.NOT_IMPLEMENTED, host);
                logger.warn("서블릿 타입 오류: {}", className, e);
            } catch (Exception e) {
                sendErrorPage(out, HttpStatus.INTERNAL_SERVER_ERROR, host);
                logger.error("서버 처리 중 예외 발생", e);
            }

        } catch (IOException e) {
            logger.warn("입출력 오류: {}", e.getMessage(), e);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.warn("소켓 닫기 실패: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 오류 페이지 전송 유틸리티
     */
    private void sendErrorPage(OutputStream out, HttpStatus status, String host) {
        InputStream errorStream = null;

        if (host != null && config.getHosts().containsKey(host)) {
            ServerConfig.HostConfig hostConfig = config.getHosts().get(host);
            String errorFileName = config.getErrorPage(host, status.code());
            if (errorFileName != null) {
                String fullPath = hostConfig.getHttpRoot() + "/" + errorFileName;
                errorStream = getClass().getClassLoader().getResourceAsStream(fullPath);
                logger.warn("에러 페이지 경로: {}", fullPath);
            }
        }

        errorHandler.send(out, status, errorStream);
    }
}
