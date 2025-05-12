package com.nhn.web.server.service;

import com.nhn.web.server.HttpRequest;
import com.nhn.web.server.HttpResponse;
import com.nhn.web.server.servlet.SimpleServlet;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time implements SimpleServlet {
    @Override
    public void service(HttpRequest req, HttpResponse res) {
        try {
            Writer writer = res.getWriter();
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.write("<!DOCTYPE html>");
            writer.write("<html><head><title>Current Time</title>");
            writer.write("<style>body { font-family: Arial; padding: 20px; }</style>");
            writer.write("</head><body>");
            writer.write("<h1>Current Time</h1>");
            writer.write("<p>The current server time is: <strong>" + currentTime + "</strong></p>");
            writer.write("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
