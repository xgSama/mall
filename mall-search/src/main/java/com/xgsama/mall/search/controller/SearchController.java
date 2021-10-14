package com.xgsama.mall.search.controller;

import com.xgsama.mall.search.vo.SearchParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * SearchController
 *
 * @author : xgSama
 * @date : 2021/10/6 18:19:52
 */
@Controller
public class SearchController {

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model, HttpServletRequest request) {
        model.addAttribute("result", "result");
        return "index";
    }
}
