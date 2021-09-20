package com.xgsama.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.product.entity.AttrEntity;
import com.xgsama.mall.product.vo.AttrGroupRelationVo;
import com.xgsama.mall.product.vo.AttrRespVo;
import com.xgsama.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-09 19:51:53
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVo);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    PageUtils getNoRelation(Long attrgroupId, Map<String, Object> params);
}

