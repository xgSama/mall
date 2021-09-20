package com.xgsama.mall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * SpuItemAttrGroup
 *
 * @author : xgSama
 * @date : 2021/9/20 12:00:09
 */
@ToString
@Data
public class SpuItemAttrGroup {
    private String groupName;

    private List<SpuBaseAttrVo> attrs;
}