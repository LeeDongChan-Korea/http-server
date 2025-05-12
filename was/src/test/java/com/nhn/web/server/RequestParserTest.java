package com.nhn.web.server;

import org.junit.Test;
import java.io.BufferedReader;
import java.io.StringReader;
import static org.junit.Assert.*;

/**
 * Tests for RequestParser
 */
public class RequestParserTest {

    @Test
    public void testGET() throws Exception {
        String raw = "GET /hello?name=chat HTTP/1.1\r\nHost: localhost\r\n\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(raw));

        HttpRequest req = RequestParser.parseRequest(reader);

        assertNotNull(req);
        assertEquals("/hello", req.getPath());
        assertEquals("chat", req.getParameter("name"));
        assertEquals("localhost", req.getHost());
    }

    @Test
    public void testPOST() throws Exception {
        String raw = "POST /submit HTTP/1.1\r\nHost: localhost\r\n\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(raw));

        HttpRequest req = RequestParser.parseRequest(reader);

        assertNotNull(req);
        assertEquals("POST", req.getMethod());
        assertEquals("/submit", req.getPath());
        assertEquals("localhost", req.getHost());
    }

    @Test
    public void testNullRequest() throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(""));

        HttpRequest req = RequestParser.parseRequest(reader);

        assertNull(req);
    }
}
