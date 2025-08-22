package com.kms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        // 先查出所有子节点
        List<CategoryDO> children = categoryMapper.selectList(
                new QueryWrapper<CategoryDO>().eq("parent_id", id)
        );

        // 递归删除子节点
        for (CategoryDO child : children) {
            delete(child.getId());
        }

        // 最后删除自己
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
