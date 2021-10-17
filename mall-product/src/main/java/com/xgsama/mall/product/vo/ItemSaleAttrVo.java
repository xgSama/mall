package com.xgsama.mall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * ItemSaleAttrVo
 *
 * @author : xgSama
 * @date : 2021/10/17 16:25:45
 */
@Data
public class ItemSaleAttrVo {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;
}
