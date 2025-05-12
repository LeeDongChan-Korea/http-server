package com.nhn.web.server;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Security Rule Test
 */
public class SecurityManagerTest {

    @Test
    public void testRootDined() {
        assertTrue(SecurityManager.isForbidden("/../../etc/passwd"));
    }

    @Test
    public void testExeDined() {
        assertTrue(SecurityManager.isForbidden("/malware.exe"));
    }

    @Test
    public void testAccessPath() {
        assertFalse(SecurityManager.isForbidden("/index.html"));
    }

    @Test
    public void testUrlDecode() {
        assertTrue(SecurityManager.isForbidden("/%2e%2e/secret.txt"));
    }
}
