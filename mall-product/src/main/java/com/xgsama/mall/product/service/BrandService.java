package com.xgsama.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-09 19:51:53
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

