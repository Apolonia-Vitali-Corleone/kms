package com.kms.service;

import com.kms.dto.AttachmentDTO;

import java.util.List;

public interface AttachmentService {
    void saveBatch(Long knowledgeId, List<AttachmentDTO> attachments);
    List<AttachmentDTO> listByKnowledgeId(Long knowledgeId);
    void removeByKnowledgeId(Long knowledgeId);
    void removeById(Long id);
}

