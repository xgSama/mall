package com.xgsama.common.to.es;

import lombok.Data;

/**
 * SkuHasStockVo：存储这个sku是否有库存
 *
 * @author : xgSama
 * @date : 2021/10/6 17:21:00
 */
@Data
public class SkuHasStockVo {
    private Long skuId;

    private Boolean hasStock;
}
