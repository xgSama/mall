package com.xgsama.mall.cart.service;

import com.xgsama.mall.cart.vo.Cart;
import com.xgsama.mall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

/**
 * CartService
 *
 * @author : xgSama
 * @date : 2021/10/30 11:13:06
 */
public interface CartService {

    /**
     * 将商品添加到购物车
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车中某个购物项
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取整个购物车
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     */
    void clearCart(String cartKey);
}
