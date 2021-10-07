package com.xgsama.mall.product.feign;

import com.xgsama.common.to.es.SkuEsModel;
import com.xgsama.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * SearchFeignService
 *
 * @author : xgSama
 * @date : 2021/10/6 18:33:56
 */
@FeignClient("mall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);

    @GetMapping("/search/save/test")
    String test();
}
