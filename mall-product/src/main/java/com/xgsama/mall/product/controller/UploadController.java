package com.xgsama.mall.product.controller;

import com.xgsama.common.utils.R;
import com.xgsama.mall.product.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * UploadController
 *
 * @author : xgSama
 * @date : 2021/9/12 19:54:31
 */
@RestController
@RequestMapping("product/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @RequestMapping("/one")
    public R upload1(MultipartFile multipartFile) {
        String s = "failed";
        try {

            s = uploadService.putObject(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok().put("data", s);
    }


}
