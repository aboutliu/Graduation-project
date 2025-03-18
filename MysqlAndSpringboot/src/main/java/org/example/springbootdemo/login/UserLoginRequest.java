package org.example.springbootdemo.login;

public class UserLoginRequest {
    private String name;
    private String password;

    // 构造器
    public UserLoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Getters 和 Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}