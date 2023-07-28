package top.kelecc.weMedia.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 可乐
 * @version 1.0
 * @description:
 * @date 2023/7/26 16:35
 */
@SpringBootTest(classes = top.kelecc.weMedia.WeMediaApplication.class)
public class WmNewsAutoScanServiceTest {
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Test
    public void autoScanWmNews() {
        wmNewsAutoScanService.autoScanWmNews(6233);
        wmNewsAutoScanService.autoScanWmNews(6234);
    }
}
