package com.nhn.web.server.config;

import org.junit.Before;
import org.junit.Test;

import com.nhn.web.server.config.ServerConfig.HostConfig;

import java.io.File;

import static org.junit.Assert.*;

public class ServerConfigTest {
    private ServerConfig config;

    @Before
    public void setUp() {
        config = ServerConfig.loadFromFile("test-config.json");
    }

    @Test
    public void testPortLoading() {
        assertEquals(8000, config.getPort());
    }

    @Test
    public void testHttpRootForHost() {
        assertEquals("www/a", config.getHttpRoot("a.com"));
        assertEquals("www/b", config.getHttpRoot("b.com"));
    }

    @Test
    public void testErrorPageMapping() {
        assertEquals("403.html", config.getErrorPage("a.com", 403));
        assertEquals("404.html", config.getErrorPage("a.com", 404));
        assertEquals("500.html", config.getErrorPage("b.com", 500));
    }

    @Test
    public void testDefaultFallback() {
    	HostConfig hostConfig = config.getHosts().get("a.com");
        assertEquals("403.html", hostConfig.getErrors().get("403"));
    }
}
