package com.xgsama.mall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgsama.mall.coupon.entity.HomeSubjectEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】
 * 
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 21:59:21
 */
@Mapper
public interface HomeSubjectDao extends BaseMapper<HomeSubjectEntity> {
	
}
