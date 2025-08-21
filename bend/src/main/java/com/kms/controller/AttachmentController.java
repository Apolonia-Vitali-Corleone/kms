package com.kms.controller;

import com.kms.dto.AttachmentDTO;
import com.kms.dto.AttachmentUploadResp;
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
import java.util.Collections;
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
    public AttachmentUploadResp upload(@RequestPart("file") MultipartFile file,
                                       @RequestParam("knowledgeId") Long knowledgeId) throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path dest = dir.resolve(filename);
        file.transferTo(dest.toFile());
        AttachmentDTO dto = new AttachmentDTO();
        dto.setUrl(filename);
        attachmentService.saveBatch(knowledgeId, Collections.singletonList(dto));
        AttachmentUploadResp resp = new AttachmentUploadResp();
        resp.setId(dto.getId());
        resp.setName(filename);
        resp.setUrl("/attachment/download?id=" + dto.getId());
        resp.setSize(file.getSize());
        return resp;
    }

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> download(@RequestParam("id") Long id) {
        AttachmentDTO dto = attachmentService.getByKId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        File file = Paths.get(uploadDir).resolve(dto.getUrl()).toFile();
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + dto.getUrl())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody Map<String, String> req) throws IOException {
        String id = req.get("id");
        AttachmentDTO dto = attachmentService.getByKId(Long.parseLong(id));
        attachmentService.removeById(Long.parseLong(id));
        if (dto != null) {
            Files.deleteIfExists(Paths.get(uploadDir).resolve(dto.getUrl()));
        }
    }
}

