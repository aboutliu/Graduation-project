package org.example.springbootdemo.resource.controller;

import org.example.springbootdemo.activity.model.Activity;
import org.example.springbootdemo.activity.service.ActivityService;
import org.example.springbootdemo.resource.model.Resource;
import org.example.springbootdemo.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resources")
@CrossOrigin(origins = "http://localhost:5173")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    // 获取所有活动信息
    @GetMapping
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    // 根据 ID 获取活动信息
    @GetMapping("/{id}")
    public Optional<Resource> getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id);
    }

    // 添加新活动
    @PostMapping
    public Resource createResource(@RequestBody Resource resource) {
        return resourceService.createResource(resource);
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public void deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
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