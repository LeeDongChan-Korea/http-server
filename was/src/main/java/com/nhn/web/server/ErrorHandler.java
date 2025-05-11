package com.nhn.web.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.nhn.web.server.config.HttpStatus;

/**
 * 클라이언트에게 에러 응답을 전송하는 역할을 하는 클래스
 */
public class ErrorHandler {
	private static final Logger logger = Logger.getLogger(ErrorHandler.class.getCanonicalName());
    /**
     * 지정된 HttpStatus 코드를 바탕으로 간단한 텍스트 응답을 전송한다.
     *
     * @param out    클라이언트에 응답을 보낼 OutputStream
     * @param status 전송할 HTTP 상태 코드와 메시지
     */
    public void send(OutputStream out, HttpStatus status) {
        String body = status.reason();
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        // HTTP 응답 메시지 생성
        String response = "HTTP/1.1 " + status + "\r\n" +
                          "Content-Type: text/plain; charset=UTF-8\r\n" +
                          "Content-Length: " + bodyBytes.length + "\r\n" +
                          "\r\n" +
                          body;

        try {
            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            logger.warning("Error sending error response: " + e.getMessage());
        }
    }
}
