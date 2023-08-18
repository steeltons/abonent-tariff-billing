package org.jenjetsu.com.hrs.rest;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.hrs.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/file")
@AllArgsConstructor
public class Test {

    private final MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file")MultipartFile file) {
        try {
            minioService.putObject(file.getOriginalFilename(), file.getInputStream());
            return ResponseEntity.ok("file was uploaded");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("some error");
        }
    }

    @GetMapping("/get/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable("fileName") String fileName) {
        try {
            InputStream stream = minioService.getObjectStream(fileName);
            Resource resource = new InputStreamResource(stream);
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file wasn't found");
        }
    }
}
