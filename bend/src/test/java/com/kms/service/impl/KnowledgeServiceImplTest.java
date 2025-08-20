package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kms.dto.PageReq;
import com.kms.entity.KnowledgeDO;
import com.kms.mapper.KnowledgeMapper;
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
        KnowledgeServiceImpl service = new KnowledgeServiceImpl(mapper);
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
}

