package com.xgsama.mall.product.service.impl;

import com.xgsama.mall.product.service.UploadService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * UploadServiceImpl
 *
 * @author : xgSama
 * @date : 2021/9/12 19:26:13
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Resource(name = "minioClient")
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.gateway}")
    private String gateway;

    /**
     * url分隔符
     */
    public static final String URI_DELIMITER = "/";

    @Override
    public String putObject(@RequestParam("multipartFile") MultipartFile multipartFile) throws Exception {
        return putObject(new MultipartFile[]{multipartFile}).get(0);
    }

    @Override
    public List<String> putObject(MultipartFile... multipartFiles) throws Exception {
        List<String> retVal = new LinkedList<>();
        String[] folders = getDateFolder();

        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + getSuffix(multipartFile.getOriginalFilename());

            // 年/月/日/file
            String finalPath = String.join(URI_DELIMITER, folders) +
                    URI_DELIMITER +
                    fileName;

            System.out.println(multipartFile.getContentType());

            minioClient.putObject(PutObjectArgs.builder()
                    .stream(multipartFile.getInputStream(), -1, PutObjectArgs.MAX_PART_SIZE)
                    .object(finalPath)
//                    .contentType(multipartFile.getContentType())
                    .bucket(bucket)
                    .build());

            retVal.add(gateway(finalPath));

        }

        return retVal;
    }


    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    protected static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            String suffix = fileName.substring(index + 1);
            if (!suffix.isEmpty()) {
                return suffix;
            }
        }
        throw new IllegalArgumentException("非法文件名称：" + fileName);
    }

    /**
     * 获取访问网关
     *
     * @param path
     * @return
     */
    private String gateway(String path) {
        if (!gateway.endsWith(URI_DELIMITER)) {
            gateway += URI_DELIMITER;
        }
        return gateway + path;
    }

    /**
     * 获取年月日[2020, 09, 01]
     */
    protected static String[] getDateFolder() {
        String[] retVal = new String[3];

        LocalDate localDate = LocalDate.now();
        retVal[0] = localDate.getYear() + "";

        int month = localDate.getMonthValue();
        retVal[1] = month < 10 ? "0" + month : month + "";

        int day = localDate.getDayOfMonth();
        retVal[2] = day < 10 ? "0" + day : day + "";

        return retVal;
    }
}
