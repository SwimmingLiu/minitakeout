package com.sky.config;

import com.sky.properties.QiNiuCloudProperties;
import com.sky.utils.QiNiuCloudOSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 配置类，用于创建QiNiuCloudOSSUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public QiNiuCloudOSSUtil qiNiuCloudOSSUtil(QiNiuCloudProperties qiNiuCloudProperties){
        log.info("开始创建七牛云文件上传工具类对象：{}",qiNiuCloudProperties);
        return new QiNiuCloudOSSUtil(qiNiuCloudProperties.getAccessKey(),
                qiNiuCloudProperties.getSecretKey(),
                qiNiuCloudProperties.getBucket());
    }
}