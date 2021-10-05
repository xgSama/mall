package com.xgsama.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 22:37:24
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

