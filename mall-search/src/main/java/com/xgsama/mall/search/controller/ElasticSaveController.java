package com.xgsama.mall.search.controller;

import com.xgsama.common.exception.BizCodeEnum;
import com.xgsama.common.to.es.SkuEsModel;
import com.xgsama.common.utils.R;
import com.xgsama.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * ElasticSaveController
 *
 * @author : xgSama
 * @date : 2021/10/6 18:23:13
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {


    @Autowired
    private ProductSaveService productSaveService;


    @GetMapping("/test")
    public String test() {
        System.out.println("远程调用---------------");
        return "远程调用成功";

    }

    /**
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {

        boolean status;
        try {
            status = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController商品上架错误: {}", e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (!status) {
            return R.ok();
        }
        return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
    }

}
