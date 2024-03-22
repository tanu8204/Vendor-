package com.SXN.Vendor.ResponseUtils;

public class ApiResponse<T> {
    private T data;
    private String message;
    private String error;
    private String status;
    private int statusCode;

    public ApiResponse() {
    }

    public ApiResponse(T data, String message, String error, String status, int statusCode) {
        this.data = data;
        this.message = message;
        this.error = error;
        this.status = status;
        this.statusCode = statusCode;
    }

    // Getters and setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

