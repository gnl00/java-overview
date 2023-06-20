package com.demo.proxy.serialize;

public class Frame {
    private int len;
    private int code;
    private byte[] body;

    public Frame(int len, int code, byte[] body) {
        this.len = len;
        this.code = code;
        this.body = body;
    }

    public Frame() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
