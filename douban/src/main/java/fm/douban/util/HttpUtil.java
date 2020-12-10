package fm.douban.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by pawncs on 2020/10/18.
 */
@Component
public class HttpUtil {
    Logger logger;
    public HttpUtil() {
        logger = Logger.getLogger(HttpUtil.class);
    }

    public Map<String,String> buildHeaderData(String referer,String cookie,String host){
        Map<String,String> map = new HashMap<>();
        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
        if(referer!=null && !referer.equals("")){
            map.put("Referer",referer);
        }
        if(host!= null && !host.equals("")){
            map.put("Host",host);
        }
        if(cookie!=null && !cookie.equals("")){
            map.put("Cookie",cookie);
        }

        return map;
    }
    public String getContent(String url,Map<String,String> headers) {

        Builder reqBuilder = new Builder().url(url);
        if(headers!=null && !headers.isEmpty()){
            for(String key:headers.keySet()){
                reqBuilder.addHeader(key,headers.get(key));
            }
        }
        Request request = reqBuilder.build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Call call = okHttpClient.newCall(request);
        String result = null;
        try {
            Response response = call.execute();
            result = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            logger.error("response生成失败");
            e.printStackTrace();
        }
        return result;
    }
}
