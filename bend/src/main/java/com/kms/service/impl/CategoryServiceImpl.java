package com.kms.service.impl;

import com.kms.dto.CategoryTreeNode;
import com.kms.entity.CategoryDO;
import com.kms.mapper.CategoryMapper;
import com.kms.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryTreeNode> tree() {
        List<CategoryDO> list = categoryMapper.selectList(null);
        Map<Long, CategoryTreeNode> map = new HashMap<>();
        List<CategoryTreeNode> roots = new ArrayList<>();
        for (CategoryDO c : list) {
            CategoryTreeNode node = new CategoryTreeNode();
            BeanUtils.copyProperties(c, node);
            map.put(c.getId(), node);
        }
        for (CategoryTreeNode node : map.values()) {
            Long pid = node.getParentId();
            if (pid != null && map.containsKey(pid)) {
                map.get(pid).getChildren().add(node);
            } else {
                roots.add(node);
            }
        }
        return roots;
    }

    @Override
    public void create(CategoryDO category) {
        categoryMapper.insert(category);
    }

    @Override
    public void update(CategoryDO category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        CategoryDO entity = categoryMapper.selectById(id);
        if (entity != null) {
            entity.setStatus(status);
            categoryMapper.updateById(entity);
        }
    }

    @Override
    public List<CategoryDO> list() {
        return categoryMapper.selectList(null);
    }
}
