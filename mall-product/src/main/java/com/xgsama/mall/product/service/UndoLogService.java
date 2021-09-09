package com.xgsama.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.product.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-09 19:51:52
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

