package com.kms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class KnowledgeFilterParam {
    private String title;
    private String keywords;
    private Integer status;
    private List<String> relatedCategories;
    private String tagName;
    private String visibilityName;
    private Integer questionNo;
    private LocalDate startDate;
    private LocalDate endDate;
}
