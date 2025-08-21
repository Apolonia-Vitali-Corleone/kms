package com.kms.service.impl;

import com.kms.dto.AttachmentDTO;
import com.kms.entity.AttachmentDO;
import com.kms.mapper.AttachmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceImplTest {

    @Mock
    private AttachmentMapper attachmentMapper;

    private AttachmentServiceImpl attachmentService;

    @BeforeEach
    void setUp() {
        attachmentService = new AttachmentServiceImpl(attachmentMapper);
    }

    @Test
    void saveBatch_insertsAttachmentsAndSetsId() {
        AttachmentDTO dto = new AttachmentDTO();
        dto.setUrl("file1.txt");
        when(attachmentMapper.insert(any())).thenAnswer(invocation -> {
            AttachmentDO entity = invocation.getArgument(0);
            entity.setId(1L);
            return 1;
        });

        attachmentService.saveBatch(10L, Arrays.asList(dto));

        ArgumentCaptor<AttachmentDO> captor = ArgumentCaptor.forClass(AttachmentDO.class);
        verify(attachmentMapper).insert(captor.capture());
        AttachmentDO saved = captor.getValue();
        assertEquals(10L, saved.getKnowledgeId());
        assertEquals("file1.txt", saved.getFilePath());
        assertEquals(1L, dto.getId());
    }

    @Test
    void listByKnowledgeId_returnsDtoList() {
        AttachmentDO entity = new AttachmentDO();
        entity.setId(5L);
        entity.setKnowledgeId(20L);
        entity.setFilePath("f.txt");
        when(attachmentMapper.selectList(any())).thenReturn(Collections.singletonList(entity));

        List<AttachmentDTO> list = attachmentService.listByKnowledgeId(20L);
        assertEquals(1, list.size());
        AttachmentDTO dto = list.get(0);
        assertEquals(5L, dto.getId());
        assertEquals("f.txt", dto.getUrl());
        verify(attachmentMapper).selectList(any());
    }

    @Test
    void removeByKnowledgeId_deletesMatchingRecords() {
        attachmentService.removeByKnowledgeId(3L);
        verify(attachmentMapper).delete(any());
    }

    @Test
    void removeById_deletesByPrimaryKey() {
        attachmentService.removeById(2L);
        verify(attachmentMapper).deleteById(2L);
    }
}

