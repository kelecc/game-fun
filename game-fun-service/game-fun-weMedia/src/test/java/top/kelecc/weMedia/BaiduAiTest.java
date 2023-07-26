package top.kelecc.weMedia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.kelecc.baiduAi.ImgAndTextCensor;
import top.kelecc.file.starter.service.FileStorageService;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

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
        Map<String, Object> map = this.imgCensor.textCensor("sb");
        System.out.println(map);
    }

    @Test
    public void imgCensorTest() throws FileNotFoundException {
        byte[] bytes = fileStorageService.downLoadFile("http://localhost:9000/game-fun/2023/07/17/bd31d653caa943e39e03a8df556ac4ec.png");
        Map<String, Object> map = this.imgCensor.imgCensor(bytes);
        System.out.println(map);
    }

}
