package com.xgsama.mall.search.service;

import com.xgsama.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * ProductSaveService
 *
 * @author : xgSama
 * @date : 2021/10/6 18:23:47
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
