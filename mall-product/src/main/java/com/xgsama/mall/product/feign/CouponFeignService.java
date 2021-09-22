package com.xgsama.mall.product.feign;

import com.xgsama.common.to.SkuReductionTO;
import com.xgsama.common.to.SpuBoundTO;
import com.xgsama.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CouponFeignService
 *
 * @author : xgSama
 * @date : 2021/9/22 20:21:35
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {
    /**
     * @RequestBody 将对象转换为json 将上一步的json放在请求体位置 发送请求
     * <p>
     * 对方服务收到请求。 收到的是请求体里的json数据  那边用 @RequestBody 对SpuBoundsEntity进行封装
     * <p>
     * 只要 JSON 数据模型是兼容的 双方服务无需使用同一个 TO 对象
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTO spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTO skuReductionTo);
}
