package top.kelecc.file.starter.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import top.kelecc.file.starter.config.MinioConfig;
import top.kelecc.file.starter.config.MinioConfigProperties;
import top.kelecc.file.starter.service.FileStorageService;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 11:04
 */
@Slf4j
@EnableConfigurationProperties(MinioConfigProperties.class)
@Import(MinioConfig.class)
public class FileStorageServiceImpl implements FileStorageService {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfigProperties minioConfigProperties;

    private final static String SEPARATOR = "/";

    /**
     * @param dirPath
     * @param filename yyyy/mm/dd/file.jpg
     * @return
     */
    public String builderFilePath(String dirPath, String filename) {
        StringBuilder stringBuilder = new StringBuilder(50);
        if (!StringUtils.isEmpty(dirPath)) {
            stringBuilder.append(dirPath).append(SEPARATOR);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String todayStr = sdf.format(new Date());
        stringBuilder.append(todayStr).append(SEPARATOR);
        stringBuilder.append(filename);
        return stringBuilder.toString();
    }

    /**
     * 上传图片文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public String uploadImgFile(String prefix, String filename, InputStream inputStream) {
        String filePath = builderFilePath(prefix, filename);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType("image/jpg")
                    .bucket(minioConfigProperties.getBucket()).stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            return minioConfigProperties.getReadUrl() + SEPARATOR + minioConfigProperties.getBucket() +
                    SEPARATOR +
                    filePath;
        } catch (Exception ex) {
            log.error("minio put file error.", ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 上传html文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public String uploadHtmlFile(String prefix, String filename, InputStream inputStream) {
        String filePath = builderFilePath(prefix, filename);
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType("text/html")
                    .bucket(minioConfigProperties.getBucket()).stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            return minioConfigProperties.getReadUrl() + SEPARATOR + minioConfigProperties.getBucket() +
                    SEPARATOR +
                    filePath;
        } catch (Exception ex) {
            log.error("minio put file error.", ex);
            ex.printStackTrace();
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 删除文件
     *
     * @param pathUrl 文件全路径
     */
    @Override
    public void delete(String pathUrl) {
        String key = pathUrl.replace(minioConfigProperties.getEndpoint() + "/", "");
        int index = key.indexOf(SEPARATOR);
        String bucket = key.substring(0, index);
        String filePath = key.substring(index + 1);
        // 删除Objects
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucket).object(filePath).build();
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error("minio remove file error.  pathUrl:{}", pathUrl);
            e.printStackTrace();
            throw new RuntimeException("删除文件：" + pathUrl + "失败");
        }
    }


    /**
     * 下载文件
     *
     * @param pathUrl 文件全路径
     * @return 文件流
     */
    @Override
    public byte[] downLoadFile(String pathUrl) {
        String key = pathUrl.replace(minioConfigProperties.getEndpoint() + "/", "");
        int index = key.indexOf(SEPARATOR);
        String bucket = key.substring(0, index);
        String filePath = key.substring(index + 1);
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfigProperties.getBucket()).object(filePath).build());
        } catch (Exception e) {
            log.error("minio down file error.  pathUrl:{}", pathUrl);
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while (true) {
            try {
                if (!((rc = inputStream.read(buff, 0, 100)) > 0)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
