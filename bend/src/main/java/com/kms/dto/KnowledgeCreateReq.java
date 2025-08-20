package com.kms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.kms.dto.AttachmentDTO;

@Data
public class KnowledgeCreateReq {
    @NotBlank
    private String categoryName;
    @NotBlank
    private String title;
    @NotBlank
    private String tagName;
    @NotBlank
    private String visibilityName;
    private String keywords;
    @NotNull
    private Integer status;
    private String summary;
    @NotNull
    private Integer questionNo;
    @NotBlank
    private String content;

    private List<AttachmentDTO> attachments;

    private String createdBy;
}
