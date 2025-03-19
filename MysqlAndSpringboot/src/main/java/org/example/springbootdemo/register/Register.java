package org.example.springbootdemo.register;

import org.example.springbootdemo.model.User;
import org.example.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springbootdemo.login.ApiResponse;
import org.example.springbootdemo.register.UserRegisterRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/register")
public class Register {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        System.out.println("in");
        // 检查用户名是否存在
        Optional<User> user = userService.findByName(request.getName());
        if (user.isEmpty()) {
            User existingUser = new User();
            existingUser.setName(request.getName());
            existingUser.setPassword(request.getPassword());
            existingUser.setAge(request.getAge());
            existingUser.setPhone(request.getPhone());
            existingUser.setSex(request.getSex());
            existingUser.setRoot(request.getRoot());
            CheckUserInfo checkUserInfo = new CheckUserInfo();
            ApiResponse apiResponse = checkUserInfo.validateUserInfo(existingUser);
            if (apiResponse.isSuccess()) {
                userService.createUser(existingUser);
                return ResponseEntity.ok(new ApiResponse(true, "注册成功"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "用户已存在"));
        }
    }
}