package com.nhn.web.server;

import java.net.URLDecoder;

public class SecurityManager {
    public static boolean isForbidden(String uri) {
        try {
            String decodedPath = URLDecoder.decode(uri, "UTF-8");
            return decodedPath.contains("..") || decodedPath.toLowerCase().endsWith(".exe");
        } catch (Exception e) {
            return true; 
        }
    }
}

