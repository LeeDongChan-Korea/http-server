package com.nhn.web.server.service;

import com.nhn.web.server.HttpRequest;
import com.nhn.web.server.HttpResponse;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class HelloServletTest {

    @Test
    public void testServiceOutputsName() throws Exception {
        Hello servlet = new Hello();
        HttpRequest req = new HttpRequest("/Hello?name=John",null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponse res = new HttpResponse(out);
        servlet.service(req, res);
        res.flush();

        String response = out.toString("UTF-8");
        assertTrue(response.contains("Hello, John"));
    }
}
