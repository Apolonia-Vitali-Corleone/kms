package com.kms.controller;

import com.kms.common.R;
import com.kms.service.VisibilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visibilities")
public class VisibilityController {

    private final VisibilityService visibilityService;

    public VisibilityController(VisibilityService visibilityService) {
        this.visibilityService = visibilityService;
    }

    @GetMapping
    public R<?> listAll() {
        return R.ok(visibilityService.listAll());
    }
}
