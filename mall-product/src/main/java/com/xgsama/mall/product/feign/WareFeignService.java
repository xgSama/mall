package com.xgsama.mall.product.feign;

import com.xgsama.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * WareFeignService
 *
 * @author : xgSama
 * @date : 2021/10/6 18:03:49
 */
@FeignClient("mall-ware")
public interface WareFeignService {

    /**
     * 修改真个系统的 R 带上泛型
     */
    @PostMapping("/ware/waresku/hasStock")
//	List<SkuHasStockVo> getSkuHasStock(@RequestBody List<Long> SkuIds);
    R getSkuHasStock(@RequestBody List<Long> SkuIds);
}
