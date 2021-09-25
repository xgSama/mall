package com.xgsama.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.ware.entity.PurchaseEntity;
import com.xgsama.mall.ware.vo.MergeVo;

import java.util.Map;

/**
 * 采购信息
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 22:37:24
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivedPurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);
}

