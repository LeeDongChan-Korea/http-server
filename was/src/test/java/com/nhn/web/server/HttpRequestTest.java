package com.nhn.web.server;

import org.junit.Test;
import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void testParseUriWithParams() {
        HttpRequest req = new HttpRequest("/hello?name=world",null);
        assertEquals("/hello", req.getPath());
        assertEquals("world", req.getParameter("name"));
    }

    @Test
    public void testUriWithoutParams() {
        HttpRequest req = new HttpRequest("/hello",null);
        assertEquals("/hello", req.getPath());
        assertNull(req.getParameter("name"));
    }

    @Test
    public void testUriWithEmptyValue() {
        HttpRequest req = new HttpRequest("/hello?name=",null);
        assertEquals("", req.getParameter("name"));
    }
}
