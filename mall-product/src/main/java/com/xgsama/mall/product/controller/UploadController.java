package com.xgsama.mall.product.controller;

import com.xgsama.common.utils.R;
import com.xgsama.mall.product.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * UploadController
 *
 * @author : xgSama
 * @date : 2021/9/12 19:54:31
 */
@RestController
@RequestMapping("product/upload")
@Slf4j
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @RequestMapping("/one")
    public R upload1(@RequestParam("file") MultipartFile multipartFile) {
        Map<String, String> stringStringMap = new HashMap<>();
        try {
            stringStringMap = uploadService.putObject(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok().put("data", stringStringMap);
    }

    @RequestMapping("/more")
    public R uploadMore(@RequestParam("files") MultipartFile[] multipartFiles) {
        List<Map<String, String>> stringStringMap = new ArrayList<>();
        try {
           stringStringMap = uploadService.putObject(multipartFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok().put("data", stringStringMap);
    }


}
