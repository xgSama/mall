package com.xgsama.mall.product.dao;

import com.xgsama.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-09 19:51:53
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
