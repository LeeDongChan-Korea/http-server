package com.nhn.web.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * HTTP 응답을 구성하고 출력하는 클래스.
 * - 기본적으로 200 OK 응답과 HTML 컨텐츠 타입을 전송한다.
 * - 본문 작성을 위한 Writer를 제공한다.
 */
public class HttpResponse {
    private final Writer writer;

    /**
     * HTTP 응답의 초기 상태(헤더 포함)를 구성한다.
     *
     * @param out 클라이언트로 데이터를 전송할 OutputStream
     * @throws IOException 스트림 쓰기 실패 시
     */
    public HttpResponse(OutputStream out) throws IOException {
        this.writer = new OutputStreamWriter(out, "UTF-8");
        
        // 기본 응답 헤더 작성 (200 OK, HTML 응답)
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: text/html; charset=utf-8\r\n\r\n");
    }

    /**
     * 응답 본문을 작성할 수 있는 Writer 객체를 반환한다.
     *
     * @return Writer 객체
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * 응답 데이터를 클라이언트로 전송한다.
     *
     * @throws IOException 스트림 플러시 실패 시
     */
    public void flush() throws IOException {
        writer.flush();
    }
}
