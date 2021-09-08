package com.xgsama.mall.order.dao;

import com.xgsama.mall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 22:30:46
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
