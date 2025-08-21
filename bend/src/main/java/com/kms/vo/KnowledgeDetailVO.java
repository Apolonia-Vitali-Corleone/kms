package com.kms.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kms.dto.AttachmentDTO;
import com.kms.entity.AttachmentDO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class KnowledgeDetailVO {

    private String title;

    private String tagName;

    private String visibilityName;

    private String keywords;

    private Integer status;

    private String summary;

    private Integer questionNo;

    private String content;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<AttachmentDTO> attachments;
}
