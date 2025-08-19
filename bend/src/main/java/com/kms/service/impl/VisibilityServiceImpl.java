package com.kms.service.impl;

import com.kms.entity.VisibilityDO;
import com.kms.mapper.VisibilityMapper;
import com.kms.service.VisibilityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisibilityServiceImpl implements VisibilityService {

    private final VisibilityMapper visibilityMapper;

    public VisibilityServiceImpl(VisibilityMapper visibilityMapper) {
        this.visibilityMapper = visibilityMapper;
    }

    @Override
    public List<VisibilityDO> listAll() {
        return visibilityMapper.selectList(null);
    }
}
