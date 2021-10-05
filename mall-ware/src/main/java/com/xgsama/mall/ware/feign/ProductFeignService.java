package com.xgsama.mall.ware.feign;

import com.xgsama.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ProductFeignService
 *
 * @author : xgSama
 * @date : 2021/9/26 20:06:32
 */

@FeignClient("mall-product")
public interface ProductFeignService {

    /**
     * 远程调用 product 获取商品名称
     * <p>
     * 1)、让所有请求过网关；
     * 1、@FeignClient("mall-gateway")：给mall-gateway所在的机器发请求
     * 2、/api/product/skuinfo/info/{skuId}
     * 2）、直接让后台指定服务处理
     * 1、@FeignClient("mall-gateway")
     * 2、/product/skuinfo/info/{skuId}
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
