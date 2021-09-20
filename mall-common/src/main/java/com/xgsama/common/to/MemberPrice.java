package com.xgsama.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * MemberPrice
 *
 * @author : xgSama
 * @date : 2021/9/20 14:37:26
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}
