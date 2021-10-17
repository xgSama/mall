package com.xgsama.mall.search.service;

import com.xgsama.mall.search.vo.SearchParam;
import com.xgsama.mall.search.vo.SearchResult;

/**
 * MallService
 *
 * @author : xgSama
 * @date : 2021/10/6 18:20:20
 */
public interface MallSearchService {

    /**
     * 检索所有参数
     */
    SearchResult search(SearchParam Param);
}
