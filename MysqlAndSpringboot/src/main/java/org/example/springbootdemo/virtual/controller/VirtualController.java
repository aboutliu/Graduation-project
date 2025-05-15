package org.example.springbootdemo.virtual.controller;

import org.example.springbootdemo.virtual.model.Virtual;
import org.example.springbootdemo.virtual.repository.VirtualRepository;
import org.example.springbootdemo.virtual.service.VirtualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/virtual")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // ✅ 指定前端地址
public class VirtualController {

    @Autowired
    private VirtualService virtualService;
    @Autowired
    private VirtualRepository virtualRepository;

    // 获取所有活动信息
    @GetMapping
    public List<Virtual> getAllvirtual() {
        return virtualService.getAllvirtual();
    }

    // 根据 ID 获取活动信息
    @GetMapping("/{id}")
    public Optional<Virtual> getvirtualById(@PathVariable Long id) {
        return virtualService.getvirtualById(id);
    }

    // 添加新活动
    @PostMapping
    public Virtual createvirtual(@RequestBody Virtual virtual) {
        return virtualService.createvirtual(virtual);
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public void deletevirtual(@PathVariable Long id) {
        virtualService.deletevirtual(id);
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

    // 点赞接口
    @PutMapping("/upvote/{id}")
    public ResponseEntity<String> upvote(@PathVariable String id) {
        Virtual virtual = virtualRepository.findById(Long.valueOf(id)).orElse(null);
        if (virtual == null) return ResponseEntity.notFound().build();

        virtual.setUp(virtual.getUp() + 1);
//        System.out.println("+1");
        virtualRepository.save(virtual);
        return ResponseEntity.ok("Upvote success");
    }

    // 点踩接口
    @PutMapping("/downvote/{id}")
    public ResponseEntity<String> downvote(@PathVariable String id) {
        Virtual virtual = virtualRepository.findById(Long.valueOf(id)).orElse(null);
        if (virtual == null) return ResponseEntity.notFound().build();

//        System.out.println("-1");
        virtual.setDown(virtual.getDown() + 1);
        virtualRepository.save(virtual);
        return ResponseEntity.ok("Downvote success");
    }
}