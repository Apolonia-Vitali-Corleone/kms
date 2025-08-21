package com.kms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kms.dto.KnowledgeFilterParam;
import com.kms.entity.KnowledgeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface KnowledgeMapper extends BaseMapper<KnowledgeDO> {
    IPage<KnowledgeDO> selectPageByFilters(IPage<?> page, @Param("p") KnowledgeFilterParam p);
}
