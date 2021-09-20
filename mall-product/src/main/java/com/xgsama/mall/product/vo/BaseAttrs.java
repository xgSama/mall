package com.xgsama.mall.product.vo;

import lombok.Data;

/**
 * BaseAttrs
 *
 * @author : xgSama
 * @date : 2021/9/20 14:14:17
 */
@Data
public class BaseAttrs {

    private Long attrId;
    private String attrValues;
    /**
     * 是否快速展示
     */
    private int showDesc;

}