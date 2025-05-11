package com.nhn.web.server.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerConfig {
    private int port;
    private Map<String, HostConfig> hosts;
    private Map<String, String> defaultErrorPages;

    public int getPort() {
        return port;
    }

    public Map<String, HostConfig> getHosts() {
        return hosts;
    }

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

    public String getHttpRoot(String host) {
        HostConfig hostConfig = hosts.get(host);
        if (hostConfig != null) {
            return hostConfig.getHttpRoot();
        }
        return null;
    }

    public String getErrorPage(String host, int statusCode) {
        HostConfig hostConfig = hosts.get(host);
        if (hostConfig != null && hostConfig.getErrors() != null) {
            String page = hostConfig.getErrors().get(String.valueOf(statusCode));
            if (page != null) return page;
        }
        // fallback to defaultErrorPages
        return defaultErrorPages != null ? defaultErrorPages.get(String.valueOf(statusCode)) : null;
    }

    public static class HostConfig {
        private String httpRoot;
        private String indexFile = "index.html"; // 기본값

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
