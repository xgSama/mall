package com.xgsama.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.coupon.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 21:59:21
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

