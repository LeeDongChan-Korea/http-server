package com.nhn.web.server.servlet;

import com.nhn.web.server.config.ServerConfig;

/**
 * URL 경로를 서블릿 클래스 이름으로 매핑합니다.
 * 정적 파일(.html 등)은 null을 반환합니다.
 */
public class ServletMapper {
    private static final String BASE_PACKAGE = "com.nhn.web.server.service";

    /**
     * URL 경로를 기반으로 서블릿 클래스 이름을 생성합니다.
     *
     * @param path   요청 경로 (예: /Hello)
     * @param config 서버 설정
     * @param host   요청 호스트명
     * @return 매핑된 클래스 이름 (정적 요청이면 null)
     */
    public static String mapUrlToClassName(String path, ServerConfig config, String host) {
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
}
