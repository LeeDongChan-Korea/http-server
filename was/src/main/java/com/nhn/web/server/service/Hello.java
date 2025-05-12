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
            if (name == null || name.trim().isEmpty()) name = "Guest";

            writer.write("<!DOCTYPE html>");
            writer.write("<html><head><title>Hello</title>");
            writer.write("<style>body { font-family: Arial; padding: 20px; }</style>");
            writer.write("</head><body>");
            writer.write("<h1>Hello, " + name + "!</h1>");
            writer.write("<p>Welcome to the simple Java servlet example.</p>");
            writer.write("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
