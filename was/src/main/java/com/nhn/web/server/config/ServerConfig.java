package com.nhn.web.server.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 서버 설정 정보를 나타내는 클래스.
 * config.json을 통해 포트, 호스트, 에러 페이지 정보 등을 불러온다.
 */
public class ServerConfig {
    private int port;
    private Map<String, HostConfig> hosts;
    private Map<String, String> defaultErrorPages;

    /** @return 서버 포트 */
    public int getPort() {
        return port;
    }

    /** @return 호스트 설정 정보 */
    public Map<String, HostConfig> getHosts() {
        return hosts;
    }

    /** config.json에서 설정을 로드 */
    public static ServerConfig loadFromFile(String fileName) {
        try (InputStream is = ServerConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, ServerConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load server config", e);
        }
    }

    /** @return 호스트의 http 루트 경로 */
    public String getHttpRoot(String host) {
        HostConfig hostConfig = hosts.get(host);
        return hostConfig != null ? hostConfig.getHttpRoot() : null;
    }

    /** @return 상태 코드에 해당하는 에러 페이지 */
    public String getErrorPage(String host, int statusCode) {
        HostConfig hostConfig = hosts.get(host);
        if (hostConfig != null && hostConfig.getErrors() != null) {
            String page = hostConfig.getErrors().get(String.valueOf(statusCode));
            if (page != null) return page;
        }
        return defaultErrorPages != null ? defaultErrorPages.get(String.valueOf(statusCode)) : null;
    }

    /** 개별 호스트 설정 */
    public static class HostConfig {
        private String httpRoot;
        private String indexFile = "index.html";

        @JsonProperty("errorPages")
        private Map<String, String> errors = new HashMap<>();

        public String getHttpRoot() {
            return httpRoot;
        }

        public String getIndexFile() {
            return indexFile != null ? indexFile : "index.html";
        }

        public Map<String, String> getErrors() {
            return errors;
        }
    }
}
