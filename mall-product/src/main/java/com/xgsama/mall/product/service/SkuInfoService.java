package com.xgsama.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 21:02:19
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

