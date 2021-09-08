package com.xgsama.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 21:02:19
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

