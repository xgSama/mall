package com.xgsama.mall.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ItemController
 *
 * @author : xgSama
 * @date : 2021/10/14 20:29:38
 */
@Controller
public class ItemController {

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable String skuId) {
        System.out.println("准备查询" + skuId + "的详情");
        return "item";
    }
}
