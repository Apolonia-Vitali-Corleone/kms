package kms.controller;

import kms.common.BaseController;
import kms.mapper.CategoryMapper;
import kms.model.Category;
import kms.model.vo.CategoryTreeVO;
import kms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController<CategoryService, Category> {
    public CategoryController(CategoryService categoryService) {
        super(categoryService);
    }

    @GetMapping("/tree")
    public List<CategoryTreeVO> tree() {
        return service.buildFullTree();
    }

    @GetMapping("/{id}/childrenTree")
    public List<CategoryTreeVO> subTree(@PathVariable Long id) {
        return service.buildSubTree(id);
    }
}
