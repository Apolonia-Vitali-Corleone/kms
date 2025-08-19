package kms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kms.model.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
