package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kms.dto.AttachmentDTO;
import com.kms.dto.KnowledgeCreateReq;
import com.kms.dto.KnowledgeUpdateReq;
import com.kms.dto.PageReq;
import com.kms.entity.KnowledgeDO;
import com.kms.mapper.KnowledgeMapper;
import com.kms.service.AttachmentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KnowledgeServiceImplTest {

    @Test
    void pageAddsFilters() {
        KnowledgeMapper mapper = Mockito.mock(KnowledgeMapper.class);
        Mockito.when(mapper.selectPage(Mockito.any(Page.class), Mockito.any(QueryWrapper.class)))
                .thenReturn(new Page<>());
        KnowledgeServiceImpl service = new KnowledgeServiceImpl(mapper, Mockito.mock(AttachmentService.class));
        PageReq req = new PageReq();
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        service.page(req, null, null, null, "cat", "tag", "vis", 1, startDate, endDate);

        ArgumentCaptor<QueryWrapper<KnowledgeDO>> captor = ArgumentCaptor.forClass(QueryWrapper.class);
        Mockito.verify(mapper).selectPage(Mockito.any(Page.class), captor.capture());
        String sql = captor.getValue().getSqlSegment();

        assertTrue(sql.contains("category_name"));
        assertTrue(sql.contains("tag_name"));
        assertTrue(sql.contains("visibility_name"));
        assertTrue(sql.contains("question_no"));
        assertTrue(sql.contains("created_at >="));
        assertTrue(sql.contains("created_at <="));
    }

    @Test
    void createSavesAttachments() {
        KnowledgeMapper mapper = Mockito.mock(KnowledgeMapper.class);
        AttachmentService attachmentService = Mockito.mock(AttachmentService.class);
        KnowledgeServiceImpl service = new KnowledgeServiceImpl(mapper, attachmentService);
        KnowledgeCreateReq req = new KnowledgeCreateReq();
        req.setCategoryName("c");
        req.setTitle("t");
        req.setTagName("tag");
        req.setVisibilityName("vis");
        req.setStatus(1);
        req.setQuestionNo(1);
        req.setContent("cnt");
        req.setAttachments(java.util.Collections.singletonList(new AttachmentDTO()));
        service.create(req);
        Mockito.verify(attachmentService).saveBatch(Mockito.anyLong(), Mockito.anyList());
    }

    @Test
    void getLoadsAttachments() {
        KnowledgeMapper mapper = Mockito.mock(KnowledgeMapper.class);
        AttachmentService attachmentService = Mockito.mock(AttachmentService.class);
        KnowledgeDO entity = new KnowledgeDO();
        entity.setId(1L);
        Mockito.when(mapper.selectById(1L)).thenReturn(entity);
        Mockito.when(attachmentService.listByKnowledgeId(1L))
                .thenReturn(java.util.Collections.singletonList(new AttachmentDTO()));
        KnowledgeServiceImpl service = new KnowledgeServiceImpl(mapper, attachmentService);
        KnowledgeDO result = service.get(1L);
        Mockito.verify(attachmentService).listByKnowledgeId(1L);
        org.junit.jupiter.api.Assertions.assertEquals(1, result.getAttachments().size());
    }

    @Test
    void removeDeletesAttachments() {
        KnowledgeMapper mapper = Mockito.mock(KnowledgeMapper.class);
        AttachmentService attachmentService = Mockito.mock(AttachmentService.class);
        KnowledgeServiceImpl service = new KnowledgeServiceImpl(mapper, attachmentService);
        service.remove(1L);
        Mockito.verify(attachmentService).removeByKnowledgeId(1L);
    }

    @Test
    void updateRefreshesAttachments() {
        KnowledgeMapper mapper = Mockito.mock(KnowledgeMapper.class);
        AttachmentService attachmentService = Mockito.mock(AttachmentService.class);
        KnowledgeDO existing = new KnowledgeDO();
        Mockito.when(mapper.selectById(1L)).thenReturn(existing);
        KnowledgeServiceImpl service = new KnowledgeServiceImpl(mapper, attachmentService);
        KnowledgeUpdateReq req = new KnowledgeUpdateReq();
        req.setAttachments(java.util.Collections.singletonList(new AttachmentDTO()));
        service.update(1L, req);
        Mockito.verify(attachmentService).removeByKnowledgeId(1L);
        Mockito.verify(attachmentService).saveBatch(1L, req.getAttachments());
        Mockito.verify(mapper).updateById(Mockito.any(KnowledgeDO.class));
    }
}

