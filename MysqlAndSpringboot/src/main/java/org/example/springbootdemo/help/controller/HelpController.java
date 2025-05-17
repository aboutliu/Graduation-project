package org.example.springbootdemo.help.controller;

import org.example.springbootdemo.help.model.Help;
import org.example.springbootdemo.help.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/help")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // ✅ 指定前端地址
public class HelpController {

    @Autowired
    private HelpService helpService;

    // 获取所有活动信息
    @GetMapping
    public List<Help> getAllHelp() {
        return helpService.getAllHelp();
    }

    // 根据 ID 获取活动信息
    @GetMapping("/{id}")
    public Optional<Help> gethelpById(@PathVariable Long id) {
        return helpService.getHelpById(id);
    }

    // 添加新求助
    @PostMapping
    public Help createhelp(@RequestBody Help help) {
        return helpService.createHelp(help);
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public void deletehelp(@PathVariable Long id) {
        helpService.deleteHelp(id);
    }

    // 删除图片
    @DeleteMapping("/deleteImage")
    public void deleteImage(@RequestParam("path") String path) {
        String fullPath = "uploads/" + path.replace("/uploads/", "");
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
    }
}