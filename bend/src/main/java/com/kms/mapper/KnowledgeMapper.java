package com.kms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kms.entity.KnowledgeDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KnowledgeMapper extends BaseMapper<KnowledgeDO> {
}
