package com.kms.controller;

import com.kms.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public R<?> upload(@RequestPart("files") MultipartFile[] files) throws IOException {
        List<String> paths = new ArrayList<>();
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        for (MultipartFile file : files) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path dest = dir.resolve(filename);
            file.transferTo(dest.toFile());
            paths.add(filename);
        }
        return R.ok(paths);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<FileSystemResource> getFile(@PathVariable String filename) {
        File file = Paths.get(uploadDir).resolve(filename).toFile();
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
