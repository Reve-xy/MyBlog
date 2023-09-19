package cn.ly.framework.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 简要描述
 * minio本地上传
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/6/20 22:25
 */
@Data
@Component
@ConfigurationProperties(prefix="minio")
public class MinioConfig {
    private String bucketName;
    private String endpoint;
    private String accessKey;
    private String secretKey;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey,secretKey).build();
    }
}
