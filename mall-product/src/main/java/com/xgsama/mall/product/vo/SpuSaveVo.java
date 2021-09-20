package com.xgsama.mall.product.vo;

import com.xgsama.common.valid.SaveValid;
import com.xgsama.common.valid.UpdateVaild;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * SpuSaveVo
 *
 * @author : xgSama
 * @date : 2021/9/20 14:12:05
 */
@Data
public class SpuSaveVo {
    @NotNull(groups = {SaveValid.class, UpdateVaild.class}, message = "名称不能为空")
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
}
