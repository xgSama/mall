package com.xgsama.mall.ware.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;

import com.xgsama.mall.ware.dao.WareSkuDao;
import com.xgsama.mall.ware.entity.WareSkuEntity;
import com.xgsama.mall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    /**
     * 商品库存的模糊查询
     * skuId: 1
     * wareId: 1
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String id = (String) params.get("skuId");
        if (!StringUtils.isEmpty(id)) {
            wrapper.eq("sku_id", id);
        }
        id = (String) params.get("wareId");
        if (!StringUtils.isEmpty(id)) {
            wrapper.eq("ware_id", id);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

}