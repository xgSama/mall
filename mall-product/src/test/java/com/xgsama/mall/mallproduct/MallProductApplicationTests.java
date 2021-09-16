package com.xgsama.mall.mallproduct;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xgsama.mall.product.MallProductApplication;
import com.xgsama.mall.product.entity.BrandEntity;
import com.xgsama.mall.product.service.BrandService;
import com.xgsama.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallProductApplication.class)
public class MallProductApplicationTests {


    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setDescript("");
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);

//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("测试两下");
//        brandService.updateById(brandEntity);

//        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));
//        list.forEach(System.out::println);

        Long[] catelogPath = categoryService.findCatelogPath(225L);

        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }


}
