package com.nhn.web.server.servlet;

import com.nhn.web.server.config.ServerConfig;

import java.util.Map;

public class ServletMapper {
    private static final String BASE_PACKAGE = "com.nhn.web.server.service";

    /**
     * URL 경로를 서블릿 클래스 이름으로 매핑합니다.
     * 1. 설정 파일에 명시된 매핑이 있다면 우선 사용합니다.
     * 2. 없다면 규칙 기반으로 클래스 이름을 추론합니다.
     *
     * @param path   요청 경로 (예: /Hello)
     * @param config 서버 설정
     * @param host   요청 호스트명
     * @return 매핑된 클래스 이름 (정적 요청이면 null)
     */
    public static String mapUrlToClassName(String path, ServerConfig config, String host) {
    	
        if (path == null) {
            return null;
        }
        // 1. 설정 기반 매핑이 존재할 경우 우선 사용
        Map<String, String> servletMappings = getServletMappingsFromConfig(config);
        if (servletMappings.containsKey(path)) {
            String mappedClass = servletMappings.get(path);
            return BASE_PACKAGE + "." + mappedClass;
        }

        // 2. 규칙 기반 매핑 처리
        if (path == null || path.equals("/")) {
            String indexFile = config.getHosts().get(host).getIndexFile();
            if (indexFile.contains(".")) {
                return null;
            }
            return BASE_PACKAGE + "." + indexFile;
        }

        String trimmed = path.startsWith("/") ? path.substring(1) : path;
        if (trimmed.contains(".")) {
            return null;
        }

        return BASE_PACKAGE + "." + trimmed.replace("/", ".");
    }

    // 추후 확장을 위한 placeholder 메서드
    private static Map<String, String> getServletMappingsFromConfig(ServerConfig config) {
        // 설정 파일에서 매핑 정보를 가져오도록 확장할 수 있음
        return Map.of(); // 현재는 빈 매핑
    }
}
