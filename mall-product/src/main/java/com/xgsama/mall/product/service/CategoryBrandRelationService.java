package com.xgsama.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.product.entity.BrandEntity;
import com.xgsama.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-09 19:51:53
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryBrandRelationEntity> getBrandCateRelation(Long brandId);

    void saveIdAndName(CategoryBrandRelationEntity categoryBrandRelation);

    List<BrandEntity> getBrandByCatlogId(Long catId);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);
}

