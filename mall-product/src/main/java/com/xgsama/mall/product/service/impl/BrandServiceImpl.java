package com.xgsama.mall.product.service.impl;

import com.xgsama.mall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;

import com.xgsama.mall.product.dao.BrandDao;
import com.xgsama.mall.product.entity.BrandEntity;
import com.xgsama.mall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(key)) {

            queryWrapper.eq("brand_id", key).or().like("name", key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        // 保证冗余字段的数据一致
        this.updateById(brand);
        if (StringUtils.isNotEmpty(brand.getName())) {
            // 同步更新其他关联表中的数据
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());

            // TODO 更新其他关联
        }
    }

}