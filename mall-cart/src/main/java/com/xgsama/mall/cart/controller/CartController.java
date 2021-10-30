package com.xgsama.mall.cart.controller;

import com.xgsama.mall.cart.service.CartService;
import com.xgsama.mall.cart.vo.Cart;
import com.xgsama.mall.cart.vo.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

/**
 * CartController
 *
 * @author : xgSama
 * @date : 2021/10/30 11:09:33
 */
@Slf4j
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    private final String PATH = "redirect:http://cart.mall.com/cart.html";

    /**
     * 浏览器有一个cookie：user-key 标识用户身份 一个月后过期
     * 每次访问都会带上这个 user-key
     * 如果没有临时用户 还要帮忙创建一个
     */
    @GetMapping({"/", "/cart.html"})
    public String carListPage(Model model) throws ExecutionException, InterruptedException {

        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * RedirectAttributes: 会自动将数据添加到url后面
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {

        CartItem cartItem = cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        // 重定向到成功页面
        return "redirect:http://cart.mall.com/addToCartSuccess.html";
    }


    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam(value = "skuId", required = false) Long skuId, Model model) {
        CartItem cartItem = null;
        // 然后在查一遍 购物车
        if (skuId == null) {
            model.addAttribute("item", null);
        } else {
            try {
                cartItem = cartService.getCartItem(skuId);
            } catch (NumberFormatException e) {
                log.warn("恶意操作! 页面传来非法字符.");
            }
            model.addAttribute("cartItem", cartItem);
        }
        return "success";
    }

}
