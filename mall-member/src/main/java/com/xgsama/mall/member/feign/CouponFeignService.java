package com.xgsama.mall.member.feign;

import com.xgsama.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CouponFeignService
 *
 * @author : xgSama
 * @date : 2021/9/9 20:57:18
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {
    @RequestMapping("coupon/coupon/member/list")
    R membercoupons();
}
