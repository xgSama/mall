package com.xgsama.mall.product.controller;

import com.xgsama.common.to.es.SkuEsModel;
import com.xgsama.mall.product.feign.SearchFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * AlphaController
 *
 * @author : xgSama
 * @date : 2021/10/6 19:29:48
 */
@RestController
@RequestMapping("/test")
public class AlphaController {

    @Autowired
    SearchFeignService searchFeignService;

    @GetMapping("/es")
    public String testF() {

        List<SkuEsModel> List = new ArrayList<>();
        SkuEsModel skuEsModel = new SkuEsModel();
        skuEsModel.setSkuId(1L);
        skuEsModel.setBrandName("测试调用");
        skuEsModel.setCatalogName("你猜");
        List.add(skuEsModel);

        searchFeignService.productStatusUp(List);
        return searchFeignService.test();
    }
}
