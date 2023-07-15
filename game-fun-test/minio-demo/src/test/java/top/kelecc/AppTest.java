package top.kelecc;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kelecc.file.starter.service.FileStorageService;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
public class AppTest {
    @Resource
    private FileStorageService fileStorageService;

    @Test
    public void minioClientTest() {
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


    @Test
    public void uploadImgFileTest() throws IOException {
        String url = fileStorageService.uploadImgFile("", "kele.jpg", Files.newInputStream(Paths.get("D:\\Study\\退伍\\favicon.ico")));
        System.out.println(url);
    }

    @Test
    public void uploadHtmlFileTest() throws IOException {
        String url = fileStorageService.uploadHtmlFile("", "kele.html", Files.newInputStream(Paths.get("D:\\Study\\源码\\AdminEx - 响应式设计后台管理模版\\index.html")));
        System.out.println(url);
    }

    @Test
    public void deleteTest() {
        try {
            fileStorageService.delete("http://localhost:9000/game-fun/2023/07/15/kele.html");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void downloadTest() throws IOException {
        byte[] bytes = fileStorageService.downLoadFile("http://localhost:9000/game-fun/2023/07/15/kele.jpg");
        new FileOutputStream("D:\\kele.jpg").write(bytes);
    }
}
