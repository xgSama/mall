package com.xgsama.mall.ware.vo;

import lombok.Data;

/**
 * PurchaseItemDoneVo
 *
 * @author : xgSama
 * @date : 2021/9/26 20:00:43
 */
@Data
public class PurchaseItemDoneVo {

    /**
     * "itemId":1,"status":3,"reason":"",
     * "itemId":3,"status":4,"reason":"无货"
     */
    private Long itemId;

    private Integer status;

    private String reason;
}
