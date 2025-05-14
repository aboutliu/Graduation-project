package org.example.springbootdemo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/port")
public class PortController {

    private String frontendPort;

    // ✅ 允许所有来源来调用这个方法，前端才能告诉我它的端口
//    @CrossOrigin(origins = "*")
    @CrossOrigin(origins = "http://localhost:*", allowCredentials = "true")
    @PostMapping("/set-frontend-port")
    public String setFrontendPort(@RequestBody PortRequest portRequest) {
        this.frontendPort = portRequest.getPort();
        System.out.println("前端端口设置为：" + frontendPort);
        return "Frontend port set to " + frontendPort;
    }

    public String getFrontendPort() {
        return frontendPort;
    }
}