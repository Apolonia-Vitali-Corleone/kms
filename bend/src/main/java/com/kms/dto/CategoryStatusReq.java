package com.kms.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CategoryStatusReq {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Integer status;
}
