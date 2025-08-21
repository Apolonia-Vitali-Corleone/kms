package com.kms.controller;

import com.kms.common.R;
import com.kms.dto.CategoryStatusReq;
import com.kms.dto.CategoryTreeNode;
import com.kms.dto.IdReq;
import com.kms.entity.CategoryDO;
import com.kms.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/tree")
    public R<List<CategoryTreeNode>> tree() {
        return R.ok(categoryService.tree());
    }

    @GetMapping
    public R<List<Locale.Category>> list() {
        return R.ok(categoryService.list());
    }

    @PostMapping("/create")
    public R<?> create(@RequestBody CategoryDO category) {
        categoryService.create(category);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> update(@RequestBody CategoryDO category) {
        categoryService.update(category);
        return R.ok();
    }

    @PostMapping("/delete")
    public R<?> delete(@RequestBody IdReq req) {
        categoryService.delete(req.getId());
        return R.ok();
    }

    @PostMapping("/status")
    public R<?> updateStatus(@RequestBody CategoryStatusReq req) {
        categoryService.updateStatus(req.getId(), req.getStatus());
        return R.ok();
    }
}
