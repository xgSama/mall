package com.xgsama.mall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgsama.mall.product.entity.CategoryBrandRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌分类关联
 * 
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 21:02:20
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {
	
}
