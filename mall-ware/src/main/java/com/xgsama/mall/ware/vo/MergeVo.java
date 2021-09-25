package com.xgsama.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * MergeVo
 *
 * @author : xgSama
 * @date : 2021/9/25 22:29:16
 */
@Data
public class MergeVo {
    /**
     * 整单id
     */
    private Long purchaseId;

    /**
     * [1,2,3,4]
     * 合并项集合
     */
    private List<Long> items;
}
