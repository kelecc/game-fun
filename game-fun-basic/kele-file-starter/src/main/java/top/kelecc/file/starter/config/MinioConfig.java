package top.kelecc.file.starter.config;

import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kelecc.file.starter.service.FileStorageService;

import javax.annotation.Resource;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 11:01
 */
@Configuration
@EnableConfigurationProperties(MinioConfigProperties.class)
@ConditionalOnClass(FileStorageService.class)
public class MinioConfig {
    @Resource
    private MinioConfigProperties minioConfigProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(minioConfigProperties.getAccessKey(), minioConfigProperties.getSecretKey())
                .endpoint(minioConfigProperties.getEndpoint())
                .build();
    }
}
