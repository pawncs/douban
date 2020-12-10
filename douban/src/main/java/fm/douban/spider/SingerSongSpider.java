package fm.douban.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pawncs on 2020/10/26.
 */
@Component
public class SingerSongSpider {
    SubjectService subjectService;
    SingerService singerService;
    SongService songService;
    Logger logger;
    HttpUtil httpUtil;
    @Autowired
    public SingerSongSpider(HttpUtil httpUtil, SubjectService subjectService, SingerService singerService, SongService songService) {
        this.httpUtil = httpUtil;
        this.subjectService = subjectService;
        this.singerService = singerService;
        this.songService = songService;
        this.logger = Logger.getLogger(SingerSongSpider.class);
    }
    //@PostConstruct
    public void init(){
        doExcute();
    }
    public void doExcute(){
        getSongDataBySingers();
    }
    private void getSongDataBySingers(){
        logger.info("开始根据所有歌手爬取歌曲和相关歌手");
        String host = "douban.fm";
        String cookie = "bid=w7AcI3eV0i0; _ga=GA1.2.842490175.1603643254; _gid=GA1.2.953723139.1603643254; flag=\"ok\"";
        List<Singer> singers = singerService.getAll();
        for(int i = 0;i<10 && i<singers.size();i++){//TODO
            Singer singer = singers.get(i);
            String content = httpUtil.getContent(createUrl(singer.getId())
                    ,httpUtil.buildHeaderData(null,cookie,host));
            Map json = JSONObject.parseObject(content,Map.class);
            Map songList = (Map)json.get("songlist");
            List songs = JSONArray.parseArray(songList.get("songs").toString());
            for(Object songObject:songs){
                Map song = (Map)songObject;
                if(songService.get(song.get("sid").toString())==null){
                    Song songModel = new Song();
                    songModel.setId(song.get("sid").toString());
                    songModel.setUrl(song.get("url").toString());
                    songModel.setName(song.get("title").toString());
                    songModel.setSingerIds(new ArrayList<>());
                    List<String> singerIds = songModel.getSingerIds();
                    //添加歌手
                    List singersOfSong = JSONArray.parseArray(song.get("singers").toString());
                    for(Object object:singersOfSong){
                        Map map = (Map)object;
                        singerIds.add(map.get("id").toString());
                    }

                    songService.add(songModel);
                }

            }

            //相似歌手
            List<String> list = singer.getSimilarSingerIds();
            if(list == null || list.isEmpty()){
                Singer singer1 = new Singer();
                list = new ArrayList<>();
                singer1.setId(singer.getId());
                singer1.setSimilarSingerIds(list);
                Map relatedChannel = (Map)json.get("related_channel");
                List similarArtists = JSONArray.parseArray(relatedChannel.get("similar_artists").toString());
                for(Object object :similarArtists){
                    Map map = (Map) object;
                    list.add(map.get("id").toString());
                }
                singerService.modify(singer1);
            }

            logger.info("歌手" + singer.getId() + "爬取完成");
        }
        logger.info("歌曲和相关歌手爬取完成");
    }
    private String createUrl(String singerId){
        return "https://douban.fm/j/v2/artist/" + singerId + "/";

    }
}
