package com.kms.controller;

import com.kms.common.R;
import com.kms.dto.KnowledgeCreateReq;
import com.kms.dto.KnowledgeUpdateReq;
import com.kms.dto.PageReq;
import com.kms.service.KnowledgeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
                     @RequestParam(required = false) Integer status,
                     @RequestParam(required = false) String categoryName,
                     @RequestParam(required = false) String tagName,
                     @RequestParam(required = false) String visibilityName,
                     @RequestParam(required = false) Integer questionNo,
                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return R.ok(knowledgeService.page(pageReq, title, keywords, status,
                categoryName, tagName, visibilityName, questionNo, startDate, endDate));
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

    @DeleteMapping
    public R<?> remove(@RequestBody List<Long> ids) {
        // 如果只传一个id，也能兼容
        if (ids.size() == 1) {
            knowledgeService.remove(ids.get(0));
        } else {
            knowledgeService.removeBatch(ids);
        }
        return R.ok();
    }
}
