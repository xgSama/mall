package com.xgsama.mall.product.vo;

import com.xgsama.mall.product.entity.SkuImagesEntity;
import com.xgsama.mall.product.entity.SkuInfoEntity;
import com.xgsama.mall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * SkuItemVo
 *
 * @author : xgSama
 * @date : 2021/10/17 16:24:25
 */
@Data
public class SkuItemVo {

    /**
     * 基本信息
     */
    SkuInfoEntity info;

    boolean hasStock = true;

    /**
     * 图片信息
     */
    List<SkuImagesEntity> images;

    /**
     * 销售属性组合
     */
    List<ItemSaleAttrVo> saleAttr;

    /**
     * 介绍
     */
    SpuInfoDescEntity desc;

    /**
     * 参数规格信息
     */
    List<SpuItemAttrGroup> groupAttrs;

    /**
     * 秒杀信息
     */
    SeckillInfoVo seckillInfoVo;
}
