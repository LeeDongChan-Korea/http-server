package com.nhn.web.server.servlet;

import com.nhn.web.server.config.ServerConfig;


public class ServletMapper {
    private static final String BASE_PACKAGE = "com.nhn.web.server.service";

    public static String mapUrlToClassName(String path, ServerConfig config, String host) {
        if (path == null || path.equals("/")) {
            String indexFile = config.getHosts().get(host).getIndexFile();
            return BASE_PACKAGE + "." + indexFile.replace(".html", "");
        }

        String trimmed = path.startsWith("/") ? path.substring(1) : path;

        if (trimmed.contains(".")) {
            return "com.nhn.web.server." + trimmed;
        }

        return BASE_PACKAGE + "." + trimmed;
    }
}
