package com.nhn.web.server.config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test loading server configuration
 */
public class ServerConfigTest {

    @Test
    public void testLoadConfig() {
        ServerConfig config = ServerConfig.loadFromFile("config.json");
        assertNotNull(config);
    }

    @Test
    public void testHostsDefined() {
        ServerConfig config = ServerConfig.loadFromFile("config.json");
        assertTrue(config.getHosts().size() > 1);
    }

    @Test
    public void testLoadErrorPage() {
        ServerConfig config = ServerConfig.loadFromFile("config.json");
        String errorPage = config.getErrorPage("localhost", 404);
        assertNotNull(errorPage);
    }

    @Test
    public void testErrorPageNotDefined() {
        ServerConfig config = ServerConfig.loadFromFile("config.json");
        String errorPage = config.getErrorPage("localhost", 999); // 정의되지 않은 오류코드
        assertNull(errorPage);
    }
}
