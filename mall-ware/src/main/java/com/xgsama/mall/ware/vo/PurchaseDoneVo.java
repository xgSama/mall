package com.xgsama.mall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * PurchaseDoneVo
 *
 * @author : xgSama
 * @date : 2021/9/26 19:58:17
 */
@Data
public class PurchaseDoneVo {

    /**
     * 采购单id
     */
    @NotNull
    private Long id;

    private List<PurchaseItemDoneVo> items;
}
