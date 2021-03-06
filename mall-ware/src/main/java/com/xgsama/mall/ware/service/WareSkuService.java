package com.xgsama.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.to.es.SkuHasStockVo;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * εεεΊε­
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 22:37:23
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    double addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

