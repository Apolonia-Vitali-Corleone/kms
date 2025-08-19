package kms.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kms.mapper.CategoryMapper;
import kms.model.Category;
import kms.model.vo.CategoryTreeVO;
import kms.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    @Transactional(readOnly = true)
    public List<CategoryTreeVO> buildFullTree() {
        List<Category> list = this.list(Wrappers.<Category>lambdaQuery().select(Category::getId, Category::getName, Category::getParentId, Category::getRecommend, Category::getStatus).orderByAsc(Category::getParentId, Category::getId));

        Map<Long, CategoryTreeVO> map = new HashMap<>(list.size() * 2);
        for (Category c : list) {
            CategoryTreeVO vo = new CategoryTreeVO();
            vo.setId(c.getId());
            vo.setLabel(c.getName());
            vo.setParentId(c.getParentId());
            vo.setRecommend(c.getRecommend());
            vo.setStatus(c.getStatus());
            map.put(vo.getId(), vo);
        }
        List<CategoryTreeVO> roots = new ArrayList<>();
        for (CategoryTreeVO vo : map.values()) {
            Long pid = vo.getParentId();
            if (pid == null || pid == 0L || !map.containsKey(pid)) roots.add(vo);
            else map.get(pid).getChildren().add(vo);
        }
        Comparator<CategoryTreeVO> cmp = Comparator.comparing((CategoryTreeVO x) -> 0 == x.getRecommend()).thenComparing(CategoryTreeVO::getLabel, Comparator.nullsLast(String::compareTo));
        sortRecursively(roots, cmp);
        return roots;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTreeVO> buildSubTree(Long rootId) {
        // 复用已构建的整棵树（如果每次重建，复杂度是 O(n)）
        List<CategoryTreeVO> roots = buildFullTree();
        Deque<CategoryTreeVO> stack = new ArrayDeque<>(roots);

        while (!stack.isEmpty()) {
            CategoryTreeVO n = stack.pop();
            if (Objects.equals(n.getId(), rootId)) {
                // 找到就返回以它为根的子树（注意：返回的是原树中的引用）
                return Collections.singletonList(n);
            }
            List<CategoryTreeVO> children = n.getChildren();
            if (children != null && !children.isEmpty()) {
                for (CategoryTreeVO c : children) stack.push(c);
            }
        }
        return Collections.emptyList(); // 没找到
    }

    private void sortRecursively(List<CategoryTreeVO> nodes, Comparator<CategoryTreeVO> cmp) {
        nodes.sort(cmp);
        for (CategoryTreeVO n : nodes) {
            if (n.getChildren() != null && !n.getChildren().isEmpty()) sortRecursively(n.getChildren(), cmp);
        }
    }
}
