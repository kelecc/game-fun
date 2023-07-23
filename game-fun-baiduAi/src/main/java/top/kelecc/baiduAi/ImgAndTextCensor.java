package top.kelecc.baiduAi;


import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 图像审核接口
 *
 * @author 可乐
 */
public class ImgAndTextCensor {
    public static final String BAIDU_AI_ACCESS_TOKEN_KEY = "baidu_ai_access_token";
    @Resource
    RedisTemplate redisTemplate;

    @Value("${baiduAi.apiKey}")
    private String apiKey;
    @Value("${baiduAi.secretKey}")
    private String secretKey;

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public String imgCensor(byte[] imgData) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
        try {
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            return getAccessInfo(url, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String TextCensor(String text) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        try {
            String param = "text=" + URLEncoder.encode(text, "utf-8");

            return getAccessInfo(url, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private String getAccessInfo(String url, String param) throws IOException {
        String accessToken = getAccessToken(apiKey, secretKey);
        Request request = new Request.Builder()
                .url(url + "?access_token=" + accessToken)
                .method("POST", RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), param))
                .build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String getAccessToken(String ak, String sk) throws IOException {
        String accessToken = (String) redisTemplate.opsForValue().get(BAIDU_AI_ACCESS_TOKEN_KEY);
        if (accessToken != null) {
            Map map = (Map) JSON.parse(accessToken);
            return (String) map.get("access_token");
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + ak + "&client_secret=" + sk + "&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            String token = response.body().string();
            Map map = (Map) JSON.parse(token);
            redisTemplate.opsForValue().set(BAIDU_AI_ACCESS_TOKEN_KEY, token, (Integer) map.get("expires_in") - 3600, TimeUnit.SECONDS);
            return (String) map.get("access_token");
        }
    }

}
