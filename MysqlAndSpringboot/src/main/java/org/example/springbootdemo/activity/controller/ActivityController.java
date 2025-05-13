package org.example.springbootdemo.activity.controller;

import org.example.springbootdemo.activity.model.Activity;
import org.example.springbootdemo.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "http://localhost:5173")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // 获取所有活动信息
    @GetMapping
    public List<Activity> getAllActivities() {
        System.out.println(1);
//        int len =
        return activityService.getAllActivities();
    }

    // 根据 ID 获取活动信息
    @GetMapping("/{id}")
    public Optional<Activity> getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }

    // 添加新活动
    @PostMapping
    public Activity createActivity(@RequestBody Activity activity) {
        return activityService.createActivity(activity);
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }

    // 上传图片
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "images/";
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File saveFile = new File(uploadDir + filename);
        saveFile.getParentFile().mkdirs();
        file.transferTo(saveFile);
        return "/images/" + filename;
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

    // 更新图片（删除旧图并上传新图）
    @PostMapping("/updateImage")
    public String updateImage(@RequestParam("oldPath") String oldPath, @RequestParam("file") MultipartFile file) throws IOException {
        deleteImage(oldPath);
        return uploadImage(file);
    }
}