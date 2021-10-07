package com.xgsama.mall.product.dao;

import com.xgsama.mall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-09 19:51:52
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {
    /**
     * 修改上架成功的商品的状态
     */
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
