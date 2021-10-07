package com.xgsama.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Catelog2Vo
 *
 * @author : xgSama
 * @date : 2021/10/7 15:00:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog2Vo implements Serializable {
    private String id;

    private String name;

    private String catalog1Id;

    private List<Catalog3Vo> catalog3List;
}
