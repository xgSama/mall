package com.xgsama.mall.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * UploadService
 *
 * @author : xgSama
 * @date : 2021/9/12 19:12:13
 */
public interface UploadService {

    String putObject(MultipartFile multipartFile) throws Exception;

    List<String> putObject(MultipartFile... multipartFiles) throws Exception;
}
