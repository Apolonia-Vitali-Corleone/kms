package com.kms.service;

import com.kms.dto.KnowledgeCreateReq;
import com.kms.dto.KnowledgeUpdateReq;
import com.kms.dto.PageReq;
import com.kms.dto.PageResp;
import com.kms.entity.KnowledgeDO;

import java.time.LocalDate;

public interface KnowledgeService {
    PageResp<KnowledgeDO> page(PageReq req, String title, String keywords, Integer status,
                              String categoryName, String tagName, String visibilityName,
                              Integer questionNo, LocalDate startDate, LocalDate endDate);
    KnowledgeDO get(Long id);
    void create(KnowledgeCreateReq req);
    void update(Long id, KnowledgeUpdateReq req);
    void remove(Long id);
}
