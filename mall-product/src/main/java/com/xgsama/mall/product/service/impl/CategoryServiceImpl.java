package com.xgsama.mall.product.service.impl;

import com.xgsama.mall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;

import com.xgsama.mall.product.dao.CategoryDao;
import com.xgsama.mall.product.entity.CategoryEntity;
import com.xgsama.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> level1Menus = entities.stream()
                .filter(categoryEntity -> {
                    return categoryEntity.getParentCid() == 0;
                })
                .map(menu -> {
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                })
                .sorted(Comparator.comparingInt(categoryEntity ->
                        (categoryEntity.getSort() == null ? 0 : categoryEntity.getSort())))
                .collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查当前删除的菜单是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        CategoryEntity byId = this.getById(catelogId);
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }

    /**
     * 级联更新
     * @param category
     */
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        // 收集当前节点ID
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }

        return paths;
    }

    /**
     * 递归查找当前菜单的所有子菜单
     *
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity -> {
                    return categoryEntity.getParentCid().longValue() == root.getCatId().longValue();
                })
                .map(categoryEntity -> {
                    // 1.找到子菜单
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted(Comparator.comparingInt(categoryEntity ->
                        (categoryEntity.getSort() == null ? 0 : categoryEntity.getSort())))
                .collect(Collectors.toList());

        return children;
    }
}