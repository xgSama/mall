package com.xgsama.mall.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatisConfig
 *
 * @author : xgSama
 * @date : 2021/9/16 21:04:51
 */
@Configuration
@EnableTransactionManagement
public class MyBatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setOverflow(true);
        paginationInterceptor.setLimit(1000);

        return paginationInterceptor;
    }
}
