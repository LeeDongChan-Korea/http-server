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

    /**
     * 경로와 쿼리 파라미터를 분리 및 저장
     *
     * ex) /user?name=kim&age=20
     *  → path: /user
     *  → parameters: {name=kim, age=20}
     *
     * @param uri 클라이언트가 요청한 URI
     */
    public HttpRequest(String uri, String host) {
    	
    	this.host = host;
        // URI를 ? 기준으로 path와 query로 분리
        String[] parts = uri.split("\\?", 2);
        this.path = parts[0];

        if (parts.length > 1) {
            String query = parts[1];
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    parameters.put(kv[0], kv[1]);
                } else if (kv.length == 1) {
                    parameters.put(kv[0], null); // 값이 없는 파라미터 처리
                }
            }
        }
    }

    /**
     * 추출한 경로를 반환
     *
     * @return 요청 경로 (예: "/Hello")
     */
    public String getPath() {
        return path;
    }
    
    public String getHost() {
        return host;
    }
    
    /**
     * 지정된 이름의 쿼리 파라미터 값을 반환한다.
     *
     * @param key 파라미터 이름
     * @return 파라미터 값, 없으면 null
     */
    public String getParameter(String key) {
        return parameters.get(key);
    }
}
