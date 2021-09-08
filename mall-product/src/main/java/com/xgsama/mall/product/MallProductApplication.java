package com.xgsama.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MallProductApplication
 *
 * @author : xgSama
 * @date : 2021/9/8 21:28:17
 */
@MapperScan("com.xgsama.mail.product.dao")
@SpringBootApplication
public class MallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }
}
