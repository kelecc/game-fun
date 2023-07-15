package top.kelecc.file.starter.service;

import java.io.InputStream;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/15 11:03
 */
public interface FileStorageService {
    /**
     * 上传图片文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    public String uploadImgFile(String prefix, String filename, InputStream inputStream);

    /**
     * 上传html文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    public String uploadHtmlFile(String prefix, String filename, InputStream inputStream);

    /**
     * 删除文件
     *
     * @param pathUrl 文件全路径
     */
    public void delete(String pathUrl);

    /**
     * 下载文件
     *
     * @param pathUrl 文件全路径
     * @return
     */
    public byte[] downLoadFile(String pathUrl);
}
