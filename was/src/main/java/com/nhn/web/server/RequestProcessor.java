package com.nhn.web.server;

import com.nhn.web.server.config.HttpStatus;
import com.nhn.web.server.config.ServerConfig;
import com.nhn.web.server.servlet.ServletMapper;
import com.nhn.web.server.servlet.SimpleServlet;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
    private static final Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());
    private final ServerConfig config;
    private final Socket connection;
    private final ErrorHandler errorHandler = new ErrorHandler();

    public RequestProcessor(ServerConfig config, Socket connection) {
        this.config = config;
        this.connection = connection;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            OutputStream out = connection.getOutputStream()
        ) {
            HttpRequest req = null;
            String host = null;
            try {
                req = RequestParser.parseRequest(in);
                host = req != null ? req.getHost() : null;
            } catch (UnsupportedOperationException e) {
                sendErrorPage(out, HttpStatus.NOT_IMPLEMENTED, host);
                e.printStackTrace();
                return;
            }

            if (req == null || host == null || !config.getHosts().containsKey(host)) {
                sendErrorPage(out, HttpStatus.NOT_FOUND, host);
                return;
            }

            if (SecurityManager.isForbidden(req.getPath())) {
                sendErrorPage(out, HttpStatus.FORBIDDEN, host);
                return;
            }

            String path = req.getPath();
            ServerConfig.HostConfig hostConfig = config.getHosts().get(host);

            if (path.equals("/") || path.endsWith("/")) {
                path += hostConfig.getIndexFile();
            }

            File file = new File(hostConfig.getHttpRoot(), path);
            HttpResponse res = new HttpResponse(out);

            if (file.exists() && file.isFile()) {
                res.sendFile(file);
                res.flush();
                return;
            }

            String className = ServletMapper.mapUrlToClassName(path, config, host);
            try {
                Class<?> clazz = Class.forName(className);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                if (!(instance instanceof SimpleServlet)) {
                    throw new IllegalArgumentException("Invalid servlet type");
                }

                ((SimpleServlet) instance).service(req, res);
                res.flush();

            } catch (ClassNotFoundException e) {
                sendErrorPage(out, HttpStatus.NOT_FOUND, host);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                sendErrorPage(out, HttpStatus.NOT_IMPLEMENTED, host);
                e.printStackTrace();
            } catch (Exception e) {
                sendErrorPage(out, HttpStatus.INTERNAL_SERVER_ERROR, host);
                e.printStackTrace();
            }

        } catch (IOException e) {
            logger.warning("I/O error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.warning("Connection close failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void sendErrorPage(OutputStream out, HttpStatus status, String host) {
        File errorFile = null;
        if (host != null && config.getHosts().containsKey(host)) {
            ServerConfig.HostConfig hostConfig = config.getHosts().get(host);
            String fileName = config.getErrorPage(host, status.code());
            if (fileName != null) {
                try {
                    errorFile = new File(hostConfig.getHttpRoot(), fileName).getCanonicalFile();
                    logger.warning("Trying to read error file: " + errorFile.getAbsolutePath());
                } catch (IOException e) {
                    logger.warning("Failed to resolve canonical path for error file: " + e.getMessage());
                }
            }
        }
        errorHandler.send(out, status, errorFile);
    }
}
