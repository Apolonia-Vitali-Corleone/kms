package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kms.dto.AttachmentDTO;
import com.kms.entity.AttachmentDO;
import com.kms.mapper.AttachmentMapper;
import com.kms.service.AttachmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentMapper attachmentMapper;

    public AttachmentServiceImpl(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    public void saveBatch(Long knowledgeId, List<AttachmentDTO> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return;
        }
        for (AttachmentDTO dto : attachments) {
            AttachmentDO entity = new AttachmentDO();
            entity.setKnowledgeId(knowledgeId);
            entity.setFilePath(dto.getUrl());
            attachmentMapper.insert(entity);
            dto.setId(entity.getId());
        }
    }

    @Override
    public List<AttachmentDTO> listByKnowledgeId(Long knowledgeId) {
        List<AttachmentDO> list = attachmentMapper.selectList(
                new QueryWrapper<AttachmentDO>().eq("knowledge_id", knowledgeId));
        return list.stream().map(item -> {
            AttachmentDTO dto = new AttachmentDTO();
            BeanUtils.copyProperties(item, dto);
            dto.setUrl(item.getFilePath());
            dto.setFileName(item.getFilePath());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeByKnowledgeId(Long knowledgeId) {
        attachmentMapper.delete(new QueryWrapper<AttachmentDO>().eq("knowledge_id", knowledgeId));
    }

    @Override
    public void removeById(Long id) {
        attachmentMapper.deleteById(id);
    }
}

