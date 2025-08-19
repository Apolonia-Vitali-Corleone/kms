package com.kms.service.impl;

import com.kms.entity.TagDO;
import com.kms.mapper.TagMapper;
import com.kms.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagDO> listAll() {
        return tagMapper.selectList(null);
    }
}
