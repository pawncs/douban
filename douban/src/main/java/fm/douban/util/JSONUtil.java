package fm.douban.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fm.douban.model.Song;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pawncs on 2021/2/4.
 */
public class JSONUtil {
    //转换为Song
    public static Song json2song(String content){
        Song song = new Song();
        Map map = (Map) JSONObject.parseObject(content);
        song.setName((String) map.get("title"));
        song.setGmtCreated(LocalDateTime.now());
        song.setGmtModified(LocalDateTime.now());
        song.setId((String) map.get("sid"));
        song.setUrl((String)map.get("url"));
        song.setCover((String) map.get("picture"));
        //作者们
        List<String> singerList = new ArrayList<>();
        List list = (List) map.get("singers");
        for(Object object:list){
            Map singerMap = (Map)object;
            singerList.add((String) singerMap.get("id"));
        }
        song.setSingerIds(singerList);
        //System.out.println(song.toString());
        return song;
    }
}
