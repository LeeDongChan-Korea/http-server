package com.nhn.web.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SecurityManager {
    public static boolean isForbidden(String uri) {
        try {
            String decodedPath = URLDecoder.decode(uri, "UTF-8");
            return decodedPath.contains("..") || decodedPath.toLowerCase().endsWith(".exe");
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            return true; // 디코딩 오류도 금지로 간주
        }
    }
}

