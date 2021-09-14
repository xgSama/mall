package com.xgsama.mall.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * UploadService
 *
 * @author : xgSama
 * @date : 2021/9/12 19:12:13
 */
public interface UploadService {

    Map<String, String> putObject(MultipartFile multipartFile) throws Exception;

    List<Map<String, String>> putObject(MultipartFile... multipartFiles) throws Exception;
}
