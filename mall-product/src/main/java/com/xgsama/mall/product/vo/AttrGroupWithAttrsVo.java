package com.xgsama.mall.product.vo;

import com.xgsama.mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * AttrGroupWithAttrsVo
 *
 * @author : xgSama
 * @date : 2021/9/20 11:59:28
 */
@Data
public class AttrGroupWithAttrsVo {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 保存整个实体信息
     */
    private List<AttrEntity> attrs;
}

