package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kms.dto.KnowledgeCreateReq;
import com.kms.dto.KnowledgeUpdateReq;
import com.kms.dto.PageReq;
import com.kms.dto.PageResp;
import com.kms.entity.KnowledgeDO;
import com.kms.mapper.KnowledgeMapper;
import com.kms.service.KnowledgeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeMapper knowledgeMapper;

    public KnowledgeServiceImpl(KnowledgeMapper knowledgeMapper) {
        this.knowledgeMapper = knowledgeMapper;
    }

    @Override
    public PageResp<KnowledgeDO> page(PageReq req, String title, String keywords, Integer status) {
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
        Page<KnowledgeDO> page = knowledgeMapper.selectPage(new Page<>(req.getPage(), req.getPageSize()), qw);
        PageResp<KnowledgeDO> resp = new PageResp<>();
        resp.setTotal(page.getTotal());
        resp.setPage(page.getCurrent());
        resp.setPageSize(page.getSize());
        resp.setRecords(page.getRecords());
        return resp;
    }

    @Override
    public KnowledgeDO get(Long id) {
        return knowledgeMapper.selectById(id);
    }

    @Override
    public void create(KnowledgeCreateReq req) {
        KnowledgeDO entity = new KnowledgeDO();
        BeanUtils.copyProperties(req, entity);
        knowledgeMapper.insert(entity);
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
    }

    @Override
    public void remove(Long id) {
        knowledgeMapper.deleteById(id);
    }
}
