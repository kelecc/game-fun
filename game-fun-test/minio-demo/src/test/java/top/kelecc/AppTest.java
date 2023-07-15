package top.kelecc;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;


public class AppTest {
    @Test
    public void shouldAnswerWithTrue() {
        try (FileInputStream inputStream = new FileInputStream("G:\\BaiduNetdiskDownload\\黑马头条\\01\\讲义\\01-环境搭建、SpringCloud微服务(注册发现、服务调用、网关).md")) {
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://127.0.0.1:9000")
                    .build();

            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .contentType("text/octet-stream")
                    .bucket("game-fun")
                    .object("01224.md")
                    .stream(inputStream, inputStream.available(), -1)
                    .build();

            ObjectWriteResponse objectWriteResponse = minioClient.putObject(objectArgs);
            System.out.println(objectWriteResponse.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
