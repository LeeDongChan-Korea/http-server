package com.nhn.web.server;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청 URI에서 경로와 파라미터를 추출
 */
public class HttpRequest {
    private final String path;
    private final String host;
    private final Map<String, String> parameters = new HashMap<>();

    public HttpRequest(String uri, String host) {
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

    public String getPath() {
        return path;
    }

    public String getHost() {
        return host;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}
