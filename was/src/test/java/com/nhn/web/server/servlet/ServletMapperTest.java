package com.nhn.web.server.servlet;

import com.nhn.web.server.config.ServerConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServletMapperTest {

    private ServerConfig config;
    private String host;

    @Before
    public void setUp() {
        config = ServerConfig.loadFromFile("config.json");
        host = "localhost";
    }

    @Test
    public void testMappingPath() {
        String path = "/Hello";
        String expected = "com.nhn.web.server.service.Hello";
        assertEquals(expected, ServletMapper.mapUrlToClassName(path, config, host));
    }
    
    @Test
    public void testServletPath() {
        String path = "/service.Hello";
        String expected = "com.nhn.web.server.service.Hello";
        assertEquals(expected, ServletMapper.mapUrlToClassName(path, config, host));
    }


    @Test
    public void testNullPath() {
        assertNull(ServletMapper.mapUrlToClassName(null, config, host));
    }
}
