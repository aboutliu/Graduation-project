package org.example.springbootdemo.login;

//@Lombok
public class ApiResponse {

    private boolean success;
    private String message;
    private String id;

    // 构造器
    public ApiResponse(boolean success, String message, int id) {
        this.success = success;
        this.message = message;
        this.id = Integer.toString(id);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}