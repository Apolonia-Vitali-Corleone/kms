package com.kms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kms.entity.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {
}
