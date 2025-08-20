package com.kms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("attachment")
public class AttachmentDO {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long knowledgeId;
    private String filePath;
    private LocalDateTime createdAt;
}

