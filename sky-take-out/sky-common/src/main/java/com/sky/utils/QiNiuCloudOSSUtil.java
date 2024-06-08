package com.sky.utils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
//@Component 注意如果用了Configuration来配置，就不能再声明Component
public class QiNiuCloudOSSUtil {
    private String accessKey;
    private String secretKey;
    private String bucket;

    private static String resultfileDomain = "https://oss.swimmingliu.cn/";

    public String uploadFiles(MultipartFile file) throws Exception {

        //构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "D:\\Love Picture\\Swimming-Pic\\bentoME.jpg";
        // 获取文件的数据流
        InputStream inputStream = file.getInputStream();
        String originFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(inputStream, key, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return resultfileDomain + putRet.key;

        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }
        return "";
    }

}
