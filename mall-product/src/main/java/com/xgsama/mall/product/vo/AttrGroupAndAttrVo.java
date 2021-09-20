package com.xgsama.mall.product.vo;

import com.xgsama.mall.product.entity.AttrEntity;
import com.xgsama.mall.product.entity.AttrGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * AttrGroupAndAttrVo
 *
 * @author : xgSama
 * @date : 2021/9/20 10:51:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrGroupAndAttrVo extends AttrGroupEntity {

    /**
     * 属性列表
     */
    private List<AttrEntity> attrs;
}
