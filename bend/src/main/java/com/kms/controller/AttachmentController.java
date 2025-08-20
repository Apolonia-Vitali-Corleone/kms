package com.kms.controller;

import com.kms.dto.AttachmentDTO;
import com.kms.service.AttachmentService;
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
import java.util.Map;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Value("${upload.dir}")
    private String uploadDir;

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public AttachmentDTO upload(@RequestPart("file") MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path dest = dir.resolve(filename);
        file.transferTo(dest.toFile());
        AttachmentDTO dto = new AttachmentDTO();
        dto.setFileName(filename);
        dto.setUrl(filename);
        dto.setId(null);
        return dto;
    }

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> download(@RequestParam("id") String id) {
        File file = Paths.get(uploadDir).resolve(id).toFile();
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody Map<String, String> req) throws IOException {
        String id = req.get("id");
        attachmentService.removeById(Long.parseLong(id));
        Files.deleteIfExists(Paths.get(uploadDir).resolve(id));
    }
}

