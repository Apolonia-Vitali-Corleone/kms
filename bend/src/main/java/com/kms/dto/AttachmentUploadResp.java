package com.kms.dto;

import lombok.Data;

@Data
public class AttachmentUploadResp {
    private Long id;
    private String name;
    private String url;
    private Long size;
}
