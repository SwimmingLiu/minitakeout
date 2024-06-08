package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "qiniuyun.oss")
@Data
public class QiNiuCloudProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
}
