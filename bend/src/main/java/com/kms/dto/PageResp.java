package com.kms.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResp<T> {
    private long total;
    private long page;
    private long pageSize;
    private List<T> records;
}
