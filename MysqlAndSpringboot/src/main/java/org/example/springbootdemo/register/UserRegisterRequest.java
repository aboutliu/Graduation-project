package org.example.springbootdemo.register;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String name;
    private String password;
    private int age;
    private String phone;
    private String sex;
    private int root;

    // 构造器
    public UserRegisterRequest(String name, String password, int age, String phone, String sex, int root) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.sex = sex;
        this.root = root;
    }

    // Getters 和 Setters
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}