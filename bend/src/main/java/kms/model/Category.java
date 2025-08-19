package kms.model;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("category")
public class Category implements Serializable {

    private Long id;

    private String name;

    // 仅存父ID，不做外键约束；根节点可为 0 或 NULL
    private Long parentId;

    // 对应 TINYINT(1)，使用 boolean 映射
    private int recommend;

    private int status;

    private String remark;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
