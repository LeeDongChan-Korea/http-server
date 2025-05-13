package com.nhn.web.server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 클라이언트에 HTTP 응답을 보내는 클래스
 * - 기본적으로 200 OK와 HTML 응답 헤더를 보냄
 * - Writer를 통해 HTML 본문 작성 가능
 */
public class HttpResponse {
    private final OutputStream out;
    private final Writer writer;

    /**
     * 응답용 OutputStream 설정
     * 기본 응답 헤더는 200 OK + HTML로 시작
     *
     * @param out 클라이언트와 연결된 출력 스트림
     */
    public HttpResponse(OutputStream out) throws IOException {
        this.out = out;
        this.writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: text/html; charset=utf-8\r\n\r\n");
    }

    /**
     * HTML 본문 작성을 위한 Writer 제공
     *
     * @return Writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * 작성된 내용을 클라이언트로 전송 (flush)
     */
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * InputStream으로부터 데이터를 읽어 전송
     * - Content-Type을 직접 지정 가능
     *
     * @param in 입력 스트림
     * @param contentType 전송할 데이터의 MIME 타입
     */
    public void sendFile(InputStream in, String contentType) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] tmp = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(tmp)) != -1) {
            buffer.write(tmp, 0, bytesRead);
        }
        byte[] content = buffer.toByteArray();

        out.write(("HTTP/1.1 200 OK\r\n" +
                   "Content-Type: " + contentType + "\r\n" +
                   "Content-Length: " + content.length + "\r\n\r\n")
                   .getBytes(StandardCharsets.UTF_8));
        out.write(content);
        out.flush();
    }
}
