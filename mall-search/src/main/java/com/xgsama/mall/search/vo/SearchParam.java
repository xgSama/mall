package com.xgsama.mall.search.vo;

import java.util.List;

/**
 * SearchParam
 *  封装页面所有可能传递过来的关键字
 * @author : xgSama
 * @date : 2021/10/6 18:22:08
 */
public class SearchParam {

    /**
     * 全文匹配关键字
     */
    private String keyword;

    /**
     * 三级分类id
     */
    private Long catalog3Id;

    private String sort;

    private Integer hasStock;

    /**
     * 价格区间
     */
    private String skuPrice;

    /**
     * 品牌id 可以多选
     */
    private List<Long> brandId;

    /**
     * 按照属性进行筛选
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生所有查询属性
     */
    private String _queryString;
}
