package org.example.springbootdemo.register;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.example.springbootdemo.login.ApiResponse;
import org.example.springbootdemo.model.User;

public class CheckUserInfo {
    public ApiResponse validateUserInfo(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return new ApiResponse(false, "用户名不能为空", 0);
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return new ApiResponse(false, "密码长度至少为6位", 0);
        }
        if (user.getAge() < 0) {
            return new ApiResponse(false, "年龄不能为负数", 0);
        }
        if (user.getPhone() == null || !user.getPhone().matches("^1[3-9]\\d{9}$")) {
            return new ApiResponse(false, "手机号格式不正确", 0);
        }
        if (user.getSex() == null || (!user.getSex().equals("男") && !user.getSex().equals("女"))) {
            return new ApiResponse(false, "性别只能是 男 或 女", 0);
        }
        return new ApiResponse(true, "注册成功", 0);
    }
}