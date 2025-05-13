package com.nhn.web.server;

import com.nhn.web.server.config.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 에러 응답을 클라이언트로 보내는 클래스
 */
public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public void send(OutputStream out, HttpStatus status) {
        sendPlainText(out, status, status.reason());
    }

    public void send(OutputStream out, HttpStatus status, File file) {
        try {
            if (file != null && file.exists()) {
                byte[] body = Files.readAllBytes(file.toPath());
                String header = "HTTP/1.1 " + status + "\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        "Content-Length: " + body.length + "\r\n\r\n";
                out.write(header.getBytes(StandardCharsets.UTF_8));
                out.write(body);
                out.flush();
            } else {
                sendPlainText(out, status, status.reason());
            }
        } catch (IOException e) {
            logger.error("에러 페이지 전송 중 예외 발생: {}", e);
            sendPlainText(out, status, status.reason());
        }
    }

    public void send(OutputStream out, HttpStatus status, InputStream in) {
        try {
            if (in != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] tmp = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(tmp)) != -1) {
                    buffer.write(tmp, 0, bytesRead);
                }
                byte[] body = buffer.toByteArray();
                String header = "HTTP/1.1 " + status + "\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        "Content-Length: " + body.length + "\r\n\r\n";
                out.write(header.getBytes(StandardCharsets.UTF_8));
                out.write(body);
                out.flush();
            } else {
                sendPlainText(out, status, status.reason());
            }
        } catch (IOException e) {
            logger.error("에러 스트림 전송 중 문제 발생: {}", e);
            sendPlainText(out, status, status.reason());
        }
    }

    private void sendPlainText(OutputStream out, HttpStatus status, String message) {
        try {
            byte[] bodyBytes = message.getBytes(StandardCharsets.UTF_8);
            String response = "HTTP/1.1 " + status + "\r\n" +
                    "Content-Type: text/plain; charset=UTF-8\r\n" +
                    "Content-Length: " + bodyBytes.length + "\r\n\r\n" +
                    message;
            out.write(response.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            logger.error("텍스트 에러 응답 전송 실패: {}", e);
        }
    }
}
