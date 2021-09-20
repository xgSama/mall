package com.xgsama.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * SpuBoundTO
 *
 * @author : xgSama
 * @date : 2021/9/20 14:34:50
 */
@Data
public class SpuBoundTO {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;
}
