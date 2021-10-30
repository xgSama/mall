package com.xgsama.mall.cart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * CartItem
 *
 * @author : xgSama
 * @date : 2021/10/30 11:11:14
 */

public class CartItem {
    private Long skuId;

    /**
     * 是否被选中
     */
    private Boolean check = true;

    private String title;

    private String image;

    private List<String> skuAttr;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer count;

    private BigDecimal totalPrice;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(List<String> skuAttr) {
        this.skuAttr = skuAttr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 手动计算总价
     */
    public BigDecimal getTotalPrice() {
        BigDecimal res = new BigDecimal(-1);
        try {
            res = this.price.multiply(new BigDecimal("" + this.count));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
