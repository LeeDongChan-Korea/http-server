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
	    String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    String body = "<html><body><h1>Current Time: " + currentTime + "</h1></body></html>";
        try {
            Writer writer = res.getWriter();
            writer.write(body);
            writer.write("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}
