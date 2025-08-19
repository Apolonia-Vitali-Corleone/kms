package kms.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryTreeVO {
    private Long id;
    private String label;          // 前端常用：label / title
    private Long parentId;
    private int recommend;   // 可选：用于排序/展示
    private int status;         // ENABLED / DISABLED
    private List<CategoryTreeVO> children = new ArrayList<>();
}
