package com.xgsama.mall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * MallCartApplication
 *
 * @author : xgSama
 * @date : 2021/10/30 10:54:14
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCartApplication.class, args);
    }
}
