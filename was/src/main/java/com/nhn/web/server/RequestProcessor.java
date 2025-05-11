package com.nhn.web.server;

import com.nhn.web.server.config.HttpStatus;
import com.nhn.web.server.config.ServerConfig;
import com.nhn.web.server.servlet.ServletMapper;
import com.nhn.web.server.servlet.SimpleServlet;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * 클라이언트로부터 연결된 Socket을 처리
 * 요청 파싱 → 보안 검사 → 서블릿 호출 → 응답 전송
 */
public class RequestProcessor implements Runnable {
    private static final Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());

    private final ServerConfig config;
    private final Socket connection;
    private final ErrorHandler errorHandler;

    /**
     * RequestProcessor 생성자
     *
     * @param config     서버 설정 정보
     * @param connection 클라이언트와 연결된 소켓
     */
    public RequestProcessor(ServerConfig config, Socket connection) {
        this.config = config;
        this.connection = connection;
        this.errorHandler = new ErrorHandler(); // ErrorHandler는 추후 DI 구조로 전환 가능
    }

    /**
     * 클라이언트 요청을 처리
     * - 요청 파싱
     * - 보안 검사
     * - 서블릿 매핑 및 호출
     * - 에러 처리 및 응답 전송
     */
    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            OutputStream out = connection.getOutputStream()
        ) {
            HttpRequest req;

            // 요청 파싱
            try {
                req = RequestParser.parseRequest(in);
                if (req == null) {
                    errorHandler.send(out, HttpStatus.FORBIDDEN); 
                    return;
                }
            } catch (UnsupportedOperationException e) {
                errorHandler.send(out, HttpStatus.NOT_IMPLEMENTED);
                return;
            }

            // 보안 검사
            if (SecurityManager.isForbidden(req.getPath())) {
                errorHandler.send(out, HttpStatus.FORBIDDEN);
                return;
            }

            String host = req.getHost();
            String path = req.getPath();

            HttpResponse res = new HttpResponse(out);

            // 요청 경로에 해당하는 서블릿 클래스 이름 추출
            String className = ServletMapper.mapUrlToClassName(path, config, host);

            try {
                Class<?> clazz = Class.forName(className);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                if (!(instance instanceof SimpleServlet)) {
                    errorHandler.send(out, HttpStatus.NOT_IMPLEMENTED);
                    return;
                }

                ((SimpleServlet) instance).service(req, res);
                res.flush();

            } catch (ClassNotFoundException e) {
                errorHandler.send(out, HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                errorHandler.send(out, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IOException e) {
            logger.warning("I/O error: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.warning("Connection close failed: " + e.getMessage());
            }
        }
    }
}
