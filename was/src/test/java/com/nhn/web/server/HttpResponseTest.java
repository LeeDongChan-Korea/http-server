package com.nhn.web.server;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.Writer;

import static org.junit.Assert.*;

public class HttpResponseTest {

    @Test
    public void testResponseHeaderWritten() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HttpResponse res = new HttpResponse(output);
        Writer writer = res.getWriter();
        writer.write("Test");
        res.flush();

        String response = output.toString("UTF-8");
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/html; charset=utf-8"));
        assertTrue(response.contains("Test"));
    }
}
