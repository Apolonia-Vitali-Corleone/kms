package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kms.entity.CokDO;
import com.kms.mapper.CokMapper;
import com.kms.mapper.TagMapper;
import com.kms.service.CokService;
import com.kms.service.TagService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CokServiceImpl implements CokService {

    private final CokMapper cokMapper;

    public CokServiceImpl(CokMapper cokMapper) {
        this.cokMapper = cokMapper;
    }

    @Override
    public List<CokDO> listByKId(Long id) {
        return cokMapper.selectList(
                new QueryWrapper<CokDO>().eq("kid", id)
        );
    }
}
