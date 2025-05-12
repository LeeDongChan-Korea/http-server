package com.nhn.web.server;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 요청 라인과 헤더를 읽어 HttpRequest 객체를 만듦
 * - GET, POST 같은 메서드 구분은 따로 안함
 */
public class RequestParser {

    /**
     * 요청 파싱
     * 예: GET /index.html HTTP/1.1
     *
     * @param reader 클라이언트 요청을 읽을 Reader
     * @return 파싱된 HttpRequest 객체 (잘못된 요청이면 null)
     * @throws IOException 읽기 실패 시
     */
    public static HttpRequest parseRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) return null;

        String[] tokens = requestLine.split("\\s+");
        if (tokens.length < 2) return null;

        String method = tokens[0];
        String uri = tokens[1];
        String host = null;

        // 헤더 중 Host 추출
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("host:")) {
                host = line.substring(5).trim();
            }
        }

        return new HttpRequest(method, uri, host);
    }
}
