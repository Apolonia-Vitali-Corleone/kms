package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kms.dto.*;
import com.kms.service.AttachmentService;
import com.kms.entity.KnowledgeDO;
import com.kms.mapper.KnowledgeMapper;
import com.kms.service.KnowledgeService;
import com.kms.vo.KnowledgeDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeMapper knowledgeMapper;
    private final AttachmentService attachmentService;

    public KnowledgeServiceImpl(KnowledgeMapper knowledgeMapper, AttachmentService attachmentService) {
        this.knowledgeMapper = knowledgeMapper;
        this.attachmentService = attachmentService;
    }

    @Override
    public PageResp<KnowledgeDO> page(PageReq req, String title, String keywords, Integer status,
                                      String categoryName, String tagName, String visibilityName,
                                      Integer questionNo, LocalDate startDate, LocalDate endDate) {
        QueryWrapper<KnowledgeDO> qw = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            qw.like("title", title);
        }
        if (keywords != null && !keywords.isEmpty()) {
            qw.like("keywords", keywords);
        }
        if (status != null) {
            qw.eq("status", status);
        }
        if (categoryName != null && !categoryName.isEmpty()) {
            qw.eq("category_name", categoryName);
        }
        if (tagName != null && !tagName.isEmpty()) {
            qw.eq("tag_name", tagName);
        }
        if (visibilityName != null && !visibilityName.isEmpty()) {
            qw.eq("visibility_name", visibilityName);
        }
        if (questionNo != null) {
            qw.eq("question_no", questionNo);
        }
        if (startDate != null) {
            qw.ge("created_at", startDate.atStartOfDay());
        }
        if (endDate != null) {
            qw.le("created_at", endDate.atTime(23, 59, 59));
        }
        Page<KnowledgeDO> page = knowledgeMapper.selectPage(new Page<>(req.getPage(), req.getPageSize()), qw);
        PageResp<KnowledgeDO> resp = new PageResp<>();
        resp.setTotal(page.getTotal());
        resp.setPage(page.getCurrent());
        resp.setPageSize(page.getSize());
        resp.setRecords(page.getRecords());
        return resp;
    }

    @Override
    public IPage<KnowledgeDO> page(PageReq pageReq,
                                   String title,
                                   String keywords,
                                   Integer status,
                                   List<String> relatedCategories,
                                   String tagName,
                                   String visibilityName,
                                   Integer questionNo,
                                   LocalDate startDate,
                                   LocalDate endDate) {

        Page<KnowledgeDO> page = new Page<>(pageReq.getPage(), pageReq.getPageSize());

        KnowledgeFilterParam p = new KnowledgeFilterParam();
        p.setTitle(title);
        p.setKeywords(keywords);
        p.setStatus(status);
        p.setRelatedCategories(relatedCategories);
        p.setTagName(tagName);
        p.setVisibilityName(visibilityName);
        p.setQuestionNo(questionNo);
        p.setStartDate(startDate);
        p.setEndDate(endDate);

        return knowledgeMapper.selectPageByFilters(page, p);
    }

    @Override
    public KnowledgeDO get(Long id) {
        KnowledgeDO entity = knowledgeMapper.selectById(id);
        AttachmentDTO attachments = attachmentService.getByKId(id);

        KnowledgeDetailVO knowledgeDetailVO = new KnowledgeDetailVO();

        return entity;
    }

    @Override
    public void create(KnowledgeCreateReq req) {
        KnowledgeDO entity = new KnowledgeDO();
        BeanUtils.copyProperties(req, entity);
        knowledgeMapper.insert(entity);
        attachmentService.saveBatch(entity.getId(), req.getAttachments());
    }

    @Override
    public void update(Long id, KnowledgeUpdateReq req) {
        KnowledgeDO entity = knowledgeMapper.selectById(id);
        if (entity == null) {
            return;
        }
        BeanUtils.copyProperties(req, entity);
        entity.setId(id);
        knowledgeMapper.updateById(entity);
        attachmentService.removeByKnowledgeId(id);
        attachmentService.saveBatch(id, req.getAttachments());
    }

    @Override
    public void remove(Long id) {
        attachmentService.removeByKnowledgeId(id);
        knowledgeMapper.deleteById(id);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        // MyBatis-Plus 提供的批量删除
        knowledgeMapper.deleteBatchIds(ids);
        for (Long id : ids) {
            attachmentService.removeByKnowledgeId(id);
        }
    }
}
