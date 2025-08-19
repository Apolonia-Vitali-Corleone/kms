package com.kms.controller;

import com.kms.common.R;
import com.kms.dto.KnowledgeCreateReq;
import com.kms.dto.KnowledgeUpdateReq;
import com.kms.dto.PageReq;
import com.kms.service.KnowledgeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping
    public R<?> page(PageReq pageReq,
                     @RequestParam(required = false) String title,
                     @RequestParam(required = false) String keywords,
                     @RequestParam(required = false) Integer status) {
        return R.ok(knowledgeService.page(pageReq, title, keywords, status));
    }

    @GetMapping("/{id}")
    public R<?> get(@PathVariable Long id) {
        return R.ok(knowledgeService.get(id));
    }

    @PostMapping
    public R<?> create(@Validated @RequestBody KnowledgeCreateReq req) {
        knowledgeService.create(req);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<?> update(@PathVariable Long id, @Validated @RequestBody KnowledgeUpdateReq req) {
        knowledgeService.update(id, req);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<?> remove(@PathVariable Long id) {
        knowledgeService.remove(id);
        return R.ok();
    }
}
