package com.kms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class KnowledgeQueryReq extends PageReq {   // PageReq 含 page, pageSize
    private String title;
    private String keywords;
    private Integer status;
    private List<String> relatedCategories;  // 前端数组直接映射
    private String tagName;
    private String visibilityName;
    private Integer questionNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
