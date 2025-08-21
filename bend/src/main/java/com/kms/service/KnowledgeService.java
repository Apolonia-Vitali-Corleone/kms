package com.kms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kms.dto.KnowledgeCreateReq;
import com.kms.dto.KnowledgeUpdateReq;
import com.kms.dto.PageReq;
import com.kms.dto.PageResp;
import com.kms.entity.KnowledgeDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface KnowledgeService {
    PageResp<KnowledgeDO> page(PageReq req, String title, String keywords, Integer status,
                               String categoryName, String tagName, String visibilityName,
                               Integer questionNo, LocalDate startDate, LocalDate endDate);

    KnowledgeDO get(Long id);

    void create(KnowledgeCreateReq req);

    void update(Long id, KnowledgeUpdateReq req);

    void remove(Long id);

    void removeBatch(List<Long> ids);

    IPage<KnowledgeDO> page(PageReq pageReq,
                            String title,
                            String keywords,
                            Integer status,
                            List<String> relatedCategories, // AND 模式用到
                            String tagName,
                            String visibilityName,
                            Integer questionNo,
                            LocalDate startDate,
                            LocalDate endDate);
}
