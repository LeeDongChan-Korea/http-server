package com.nhn.web.server.service;

import com.nhn.web.server.servlet.SimpleServlet;
import com.nhn.web.server.HttpRequest;
import com.nhn.web.server.HttpResponse;

import java.io.IOException;
import java.io.Writer;

public class Hello implements SimpleServlet {
    @Override
    public void service(HttpRequest req, HttpResponse res) {
        try {
            Writer writer = res.getWriter();
            String name = req.getParameter("name");
            if (name == null) name = "Guest";
            writer.write("<html><body>");
            writer.write("Hello, ");
            writer.write(name);
            writer.write("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
