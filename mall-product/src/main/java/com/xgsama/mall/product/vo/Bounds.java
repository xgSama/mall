package com.xgsama.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Bounds
 *
 * @author : xgSama
 * @date : 2021/9/20 14:13:26
 */
@Data
public class Bounds {
    /**
     * 购买积分
     */
    private BigDecimal buyBounds;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;

}
