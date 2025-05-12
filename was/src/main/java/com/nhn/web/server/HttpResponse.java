package com.nhn.web.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
/**
 * HTTP 응답을 구성하고 출력하는 클래스.
 * - 기본적으로 200 OK 응답과 HTML 컨텐츠 타입을 전송한다.
 * - 본문 작성을 위한 Writer를 제공한다.
 */
public class HttpResponse {
    private final OutputStream out;
    private final Writer writer;

    public HttpResponse(OutputStream out) throws IOException {
        this.out = out;
        this.writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: text/html; charset=utf-8\r\n\r\n");
    }

    public Writer getWriter() {
        return writer;
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void sendFile(File file) throws IOException {
        byte[] content = Files.readAllBytes(file.toPath());
        out.write(("HTTP/1.1 200 OK\r\n" +
                   "Content-Type: text/html; charset=UTF-8\r\n" +
                   "Content-Length: " + content.length + "\r\n\r\n")
                   .getBytes(StandardCharsets.UTF_8));
        out.write(content);
        out.flush();
    }
}
