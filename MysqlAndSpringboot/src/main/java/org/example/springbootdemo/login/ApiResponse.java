package org.example.springbootdemo.login;

public class ApiResponse {

    private boolean success;
    private String message;

    // 构造器
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters 和 Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}