package com.xgsama.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * MemberPrice
 *
 * @author : xgSama
 * @date : 2021/9/20 14:16:29
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}