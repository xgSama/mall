package com.xgsama.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Catalog3Vo
 *
 * @author : xgSama
 * @date : 2021/10/7 15:01:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catalog3Vo {
    private String id;

    private String name;

    private String catalog2Id;
}
