package com.nhn.web.server;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청 정보 저장용 클래스.
 * 메서드, 경로, 파라미터, 호스트 정보 포함
 */
public class HttpRequest {
    private final String method;
    private final String path;
    private final String host;
    private final Map<String, String> parameters = new HashMap<>();

    /**
     * 요청 정보 파싱
     *
     * @param method GET, POST 등
     * @param uri 요청 URI (쿼리 스트링 포함 가능)
     * @param host 요청한 호스트
     */
    public HttpRequest(String method, String uri, String host) {
        this.method = method;
        this.host = host;

        String[] parts = uri.split("\\?", 2);
        this.path = parts[0];

        if (parts.length > 1) {
            for (String pair : parts[1].split("&")) {
                String[] kv = pair.split("=", 2);
                parameters.put(kv[0], kv.length > 1 ? kv[1] : null);
            }
        }
    }

    /**
     * HTTP 메서드 반환
     *
     * @return GET, POST 등
     */
    public String getMethod() {
        return method;
    }

    /**
     * 요청 경로 반환
     *
     * @return /index.html, /Hello 등
     */
    public String getPath() {
        return path;
    }

    /**
     * 요청한 호스트 반환
     *
     * @return localhost, admin.example.com 등
     */
    public String getHost() {
        return host;
    }

    /**
     * 요청 파라미터 조회
     *
     * @param key 파라미터 이름
     * @return 값 (없으면 null)
     */
    public String getParameter(String key) {
        return parameters.get(key);
    }
}
