package com.nhn.web.server;

import org.junit.Test;

import static org.junit.Assert.*;

public class SecurityManagerTest {

    @Test
    public void testPathTraversalDetected() {
        String uri = "/../../etc/passwd";
        assertTrue(SecurityManager.isForbidden(uri));
    }

    @Test
    public void testEncodedTraversalDetected() {
        String uri = "/..%2F..%2Fetc%2Fpasswd"; // URL 인코딩된 ../
        assertTrue(SecurityManager.isForbidden(uri));
    }

    @Test
    public void testExeFileDetected() {
        String uri = "/malware.exe";
        assertTrue(SecurityManager.isForbidden(uri));
    }

    @Test
    public void testNormalRequestAllowed() {
        String uri = "/index.html";
        assertFalse(SecurityManager.isForbidden(uri));
    }

    @Test
    public void testEncodedNormalPath() {
        String uri = "/hello%20world.txt";
        assertFalse(SecurityManager.isForbidden(uri));
    }

    @Test
    public void testEncodingFailure() {
        // %GG는 잘못된 인코딩이므로 IllegalArgumentException 발생
        String badUri = "/bad%GGpath";
        assertTrue(SecurityManager.isForbidden(badUri));
    }
}
