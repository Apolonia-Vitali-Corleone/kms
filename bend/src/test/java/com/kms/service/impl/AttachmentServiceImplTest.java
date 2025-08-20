package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kms.dto.AttachmentDTO;
import com.kms.entity.AttachmentDO;
import com.kms.mapper.AttachmentMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttachmentServiceImplTest {

    @Test
    void saveBatchInsertsAttachments() {
        AttachmentMapper mapper = Mockito.mock(AttachmentMapper.class);
        AttachmentServiceImpl service = new AttachmentServiceImpl(mapper);
        AttachmentDTO dto = new AttachmentDTO();
        dto.setUrl("a.txt");
        service.saveBatch(1L, Collections.singletonList(dto));
        ArgumentCaptor<AttachmentDO> captor = ArgumentCaptor.forClass(AttachmentDO.class);
        Mockito.verify(mapper).insert(captor.capture());
        assertEquals(1L, captor.getValue().getKnowledgeId());
    }

    @Test
    void listByKnowledgeIdReturnsDtos() {
        AttachmentMapper mapper = Mockito.mock(AttachmentMapper.class);
        AttachmentDO ado = new AttachmentDO();
        ado.setId(1L);
        ado.setKnowledgeId(1L);
        ado.setFilePath("a.txt");
        Mockito.when(mapper.selectList(Mockito.any(QueryWrapper.class)))
                .thenReturn(Collections.singletonList(ado));
        AttachmentServiceImpl service = new AttachmentServiceImpl(mapper);
        List<AttachmentDTO> result = service.listByKnowledgeId(1L);
        assertEquals(1, result.size());
        assertEquals("a.txt", result.get(0).getUrl());
    }

    @Test
    void removeByKnowledgeIdDeletes() {
        AttachmentMapper mapper = Mockito.mock(AttachmentMapper.class);
        AttachmentServiceImpl service = new AttachmentServiceImpl(mapper);
        service.removeByKnowledgeId(1L);
        Mockito.verify(mapper).delete(Mockito.any(QueryWrapper.class));
    }

    @Test
    void removeByIdDeletes() {
        AttachmentMapper mapper = Mockito.mock(AttachmentMapper.class);
        AttachmentServiceImpl service = new AttachmentServiceImpl(mapper);
        service.removeById(1L);
        Mockito.verify(mapper).deleteById(1L);
    }
}

