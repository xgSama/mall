package com.xgsama.mall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgsama.mall.coupon.entity.CouponHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 21:59:21
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
