package com.nhn.web.server.config;

public enum HttpStatus {
    OK(200, "OK"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String reason;

    HttpStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int code() {
        return code;
    }

    public String reason() {
        return reason;
    }

    @Override
    public String toString() {
        return code + " " + reason;
    }
}

