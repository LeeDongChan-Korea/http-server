package com.nhn.web.server.servlet;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServletMapperTest {

    @Test
    public void testSimpleClassMapping() {
        String result = ServletMapper.mapUrlToClassName("/Hello", null ,null);
        assertEquals("com.nhn.web.server.service.Hello", result);
    }

    @Test
    public void testPackageMapping() {
        String result = ServletMapper.mapUrlToClassName("/service.Hello", null, null);
        assertEquals("com.nhn.web.server.service.Hello", result);
    }
}
