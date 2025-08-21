package com.kms.service;

import com.kms.dto.CategoryTreeNode;
import com.kms.entity.CategoryDO;

import java.util.List;

public interface CategoryService {
    List<CategoryTreeNode> tree();
    void create(CategoryDO category);
    void update(CategoryDO category);
    void delete(Long id);
    void updateStatus(Long id, Integer status);
    List<CategoryDO> list();
}
