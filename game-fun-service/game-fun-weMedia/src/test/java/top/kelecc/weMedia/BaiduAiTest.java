package top.kelecc.weMedia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kelecc.baiduAi.ImgAndTextCensor;
import top.kelecc.file.starter.service.FileStorageService;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/21 14:23
 */
@SpringBootTest()
public class BaiduAiTest {
    @Resource
    private ImgAndTextCensor imgCensor;
    @Resource
    private FileStorageService fileStorageService;

    @Test
    public void textCensorTest() throws IOException {
        String s1 = this.imgCensor.TextCensor("sb");
        System.out.println(s1);
        String s2 = this.imgCensor.TextCensor("你好");
        System.out.println(s2);
    }

    @Test
    public void imgCensorTest() throws FileNotFoundException {
        byte[] bytes = fileStorageService.downLoadFile("http://localhost:9000/game-fun/2023/07/17/bd31d653caa943e39e03a8df556ac4ec.png");
        String s = this.imgCensor.imgCensor(bytes);
        System.out.println(s);
    }

}
