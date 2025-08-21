package com.kms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("cok")
@Data
public class CokDO {
    private Long id;

    private String name;
}
