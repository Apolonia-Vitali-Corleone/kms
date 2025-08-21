package com.kms.controller;

import com.kms.common.R;
import com.kms.service.CokService;
import com.kms.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coks")
public class CokController {

    private final CokService cokService;

    public CokController(CokService cokService) {
        this.cokService = cokService;
    }

    @GetMapping("/{id}")
    public R<?> listById(@PathVariable Long id) {
        return R.ok(cokService.listByKId(id));
    }
}
