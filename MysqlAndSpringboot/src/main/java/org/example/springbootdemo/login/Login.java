package org.example.springbootdemo.login;

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
import org.example.springbootdemo.login.UserLoginRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/login")
public class Login {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        // 检查用户名是否存在
        Optional<User> user = userService.findByName(request.getName());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "用户名不存在"));
        }

        User existingUser = user.get();
        // 验证密码是否正确
        if (!userService.checkPassword(request.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "密码错误"));
        }

        // 如果验证通过，返回成功的响应
        return ResponseEntity.ok(new ApiResponse(true, "登录成功"));
    }
}