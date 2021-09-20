package com.xgsama.mall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AttrRespVo
 *
 * @author : xgSama
 * @date : 2021/9/19 10:48:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrRespVo extends AttrVo {

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
