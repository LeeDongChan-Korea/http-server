package com.nhn.web.server;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * HTTP 요청의 첫 줄을 파싱하여 HttpRequest 객체를 생성
 */
public class RequestParser {

    /**
     * 요청 라인을 파싱하여 HttpRequest 객체를 반환한다.
     * 현재는 GET 메서드만 지원한다.
     *
     * 예시 요청: GET /index.html HTTP/1.1
     *
     * @param reader 입력 스트림에서 요청을 읽기 위한 BufferedReader
     * @return HttpRequest 객체 또는 유효하지 않은 경우 null
     * @throws IOException 입출력 오류
     * @throws UnsupportedOperationException GET 외의 메서드 요청 시
     */
    public static HttpRequest parseRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }

        String[] tokens = requestLine.split("\\s+");

        if (tokens.length < 2) {
            return null; // 요청 라인이 이상할 경우 null 반환
        }

        String method = tokens[0];
        String uri = tokens[1];

        if (!"GET".equals(method)) {
            throw new UnsupportedOperationException("Only GET method is supported");
        }
        
        String host = null;
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("host:")) {
                host = line.substring(5).trim(); // ex: Host: user.example.com
            }
        }

        return new HttpRequest(uri, host);
    }
}
