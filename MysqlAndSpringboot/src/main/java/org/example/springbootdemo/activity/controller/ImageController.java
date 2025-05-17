package org.example.springbootdemo.activity.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;

@RestController("activityImageController")
@RequestMapping("/activity")
//@CrossOrigin(origins = "*") // 允许跨域
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // ✅ 指定前端地址
public class ImageController {

    // 上传图片
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件为空");
        }

        try {
            // 构建目标路径
            String uploadDir = System.getProperty("user.dir") + "/images/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = file.getOriginalFilename();
            File dest = new File(uploadDir + filename);
            file.transferTo(dest);

            System.out.println("接收到上传文件：" + filename);
            System.out.println("保存路径：" + dest.getAbsolutePath());

            return ResponseEntity.ok("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上传失败");
        }
    }

    // 删除图片
    @DeleteMapping("/deleteImage/{filename}")
    public ResponseEntity<String> deleteImage(@PathVariable String filename) {
        String filePath = "images" + File.separator + filename;
        File file = new File(filePath);
        System.out.println(filePath);
        if (file.exists() && file.delete()) {
            System.out.println("图片删除成功");
            return ResponseEntity.ok("图片已删除");
        } else {
            System.out.println("图片删除失败");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到图片");
        }
    }
}