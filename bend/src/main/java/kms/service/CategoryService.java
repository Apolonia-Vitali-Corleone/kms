package kms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kms.model.Category;
import kms.model.vo.CategoryTreeVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryTreeVO> buildFullTree();
    List<CategoryTreeVO> buildSubTree(Long rootId);
}
