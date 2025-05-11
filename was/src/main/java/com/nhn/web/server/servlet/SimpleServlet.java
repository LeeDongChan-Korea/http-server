package com.nhn.web.server.servlet;

import com.nhn.web.server.HttpRequest;
import com.nhn.web.server.HttpResponse;

public interface SimpleServlet {
    void service(HttpRequest req, HttpResponse res);
}
