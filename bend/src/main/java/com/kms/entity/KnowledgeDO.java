package com.kms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.kms.dto.AttachmentDTO;

@Data
@TableName("knowledge")
public class KnowledgeDO {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    @TableField("category_name")
    private String categoryName;

    private String tagName;

    private String visibilityName;

    private String keywords;

    private Integer status;

    private String summary;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer questionNo;

    private String content;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private List<AttachmentDTO> attachments;

}
