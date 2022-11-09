package com.globits.da.support;

public class Response<T> {
    private T data;
    private int code;
    private String message;

    public Response() {
    }
    public Response( StatusMessage status) {
        this.code = status.getCode();
        this.message = status.getMessage();

    }
    public Response(T data, StatusMessage status) {
        this.data = data;
        this.code = status.getCode();
        this.message = status.getMessage();

    }



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
