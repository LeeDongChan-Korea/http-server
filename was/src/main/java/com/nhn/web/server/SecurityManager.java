package com.nhn.web.server;

import java.net.URLDecoder;

/**
 * 요청 URI에 대한 보안 검사 유틸리티 클래스
 */
public class SecurityManager {

    /**
     * URI가 허용되지 않은 경로를 포함하는지 검사합니다.
     * - 디렉토리 역참조("..")나 실행파일(.exe) 요청 차단
     * - 디코딩 오류 시에도 기본적으로 차단
     *
     * @param uri 요청 URI
     * @return 금지된 경로면 true, 허용되면 false
     */
    public static boolean isForbidden(String uri) {
        try {
            String decodedPath = URLDecoder.decode(uri, "UTF-8");
            return decodedPath.contains("..") || decodedPath.toLowerCase().endsWith(".exe");
        } catch (Exception e) {
            // 디코딩 불가한 경우도 보안상 차단
            return true;
        }
    }
}
