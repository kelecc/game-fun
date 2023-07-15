package top.kelecc.file.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 10:59
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties implements Serializable {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String readUrl;
}
