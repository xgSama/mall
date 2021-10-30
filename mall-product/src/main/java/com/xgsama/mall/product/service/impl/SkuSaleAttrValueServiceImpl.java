package com.xgsama.mall.product.service.impl;

import com.xgsama.mall.product.vo.ItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;

import com.xgsama.mall.product.dao.SkuSaleAttrValueDao;
import com.xgsama.mall.product.entity.SkuSaleAttrValueEntity;
import com.xgsama.mall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ItemSaleAttrVo> getSaleAttrsBuSpuId(Long spuId) {
        SkuSaleAttrValueDao dao = this.baseMapper;
        List<ItemSaleAttrVo> saleAttrsBuSpuId = dao.getSaleAttrsBuSpuId(spuId);
        return saleAttrsBuSpuId;
    }

    @Override
    public List<String> getSkuSaleAttrValueAsStringList(Long skuId) {
        SkuSaleAttrValueDao dao = this.baseMapper;

        return dao.getSkuSaleAttrValueAsStringList(skuId);
    }

}