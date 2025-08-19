package com.kms.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PageReq {
    @Min(1)
    private long page = 1;
    @Min(1)
    private long pageSize = 10;
}
