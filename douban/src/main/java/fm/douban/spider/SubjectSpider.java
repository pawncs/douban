package fm.douban.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fm.douban.model.Singer;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import fm.douban.util.SubjectUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pawncs on 2020/10/18.
 */
@Component
public class SubjectSpider {
    SubjectService subjectService;
    SingerService singerService;
    Logger logger;
    HttpUtil httpUtil;
    @Autowired
    public SubjectSpider(SubjectService subjectService, SingerService singerService, HttpUtil httpUtil) {
        this.subjectService = subjectService;
        this.singerService = singerService;
        logger = Logger.getLogger(SubjectSpider.class);
        this.httpUtil = httpUtil;
    }
    //系统启动的时候自动执行爬取任务
    //@PostConstruct
    public void init(){
        doExcute();
    }
    //开始爬取任务
    public void doExcute(){
        getSubjectData();
        getCollectionsData();
    }
    //执行爬取主题数据
    private void getSubjectData(){
        logger.info("开始爬取主题数据");
        try {
            String urlString = "https://douban.fm/j/v2/rec_channels?specific=all";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is;
            if(connection.getResponseCode() == 200){
                is = connection.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while((temp = bf.readLine()) != null){
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                Map data = (Map)JSONObject.parseObject(sbf.toString(),Map.class).get("data");
                if(data == null){
                    logger.error("不存在data!");
                    return;
                }
                Map channels = (Map)data.get("channels");
                if(channels == null){
                    logger.error("不存在channels");
                    return;
                }
                //歌手
                List artists = JSONObject.parseObject(channels.get("artist").toString(), List.class);
                for(Object artistObject:artists){
                    Map artist = (Map)artistObject;
                    Singer singer = new Singer();
                    singer.setId(artist.get("artist_id").toString());
                    singer.setAvatar(artist.get("cover").toString());
                    if(singerService.get(singer.getId()) != null)continue;
                    List relatedArtists = JSONObject.parseObject(artist.get("related_artists").toString(),List.class);
                    Map map = (Map)(relatedArtists.get(0));
                    singer.setName(map.get("name").toString());
                    if(singer.getSimilarSingerIds() == null){
                        singer.setSimilarSingerIds(new ArrayList<>());
                    }
                    List similar = singer.getSimilarSingerIds();
                    for(Object relatedArtistsObject:relatedArtists){
                        Map relatedArtist = (Map)relatedArtistsObject;
                        similar.add(relatedArtist.get("id").toString());
                    }
                    singerService.addSinger(singer);
                }
//                //年代/语言
//                List subjects = JSONObject.parseArray(channels.get("language").toString());
//                for(Object subjectObject:subjects){
//                    Map subjectMap = (Map) subjectObject;
//                    Subject subject = new Subject();
//                    subject.setId(subjectMap.get("id").toString());
//                    if(subjectService.get(subject.getId())!= null)continue;
//                    logger.info(subjectObject.toString());
//                    subject.setName(subjectMap.get("name").toString());
//                    subject.setDescription(subjectMap.get("intro").toString());
//                    subject.setPublishedDate(LocalDate.now());
//                    subject.setCover(subjectMap.get("cover").toString());
//                    subject.setMaster(((Map)subjectMap.get("creator")).get("name").toString());
//                    subject.setSubjectType(SubjectUtil.TYPE_MHZ);
//                    subject.setSubjectSubType(SubjectUtil.TYPE_SUB_AGE);
//                    subjectService.addSubject(subject);
//                }
//                //风格流派
//                List subjects2 = JSONObject.parseArray(channels.get("genre").toString());
//                for(Object subjectObject:subjects2){
//                    Map subjectMap = (Map) subjectObject;
//                    Subject subject = new Subject();
//                    subject.setId(subjectMap.get("id").toString());
//                    if(subjectService.get(subject.getId())!= null)continue;
//                    logger.info(subjectObject.toString());
//                    subject.setName(subjectMap.get("name").toString());
//                    subject.setDescription(subjectMap.get("intro").toString());
//                    subject.setPublishedDate(LocalDate.now());
//                    subject.setCover(subjectMap.get("cover").toString());
//                    subject.setMaster(((Map)subjectMap.get("creator")).get("name").toString());
//                    subject.setSubjectType(SubjectUtil.TYPE_MHZ);
//                    subject.setSubjectSubType(SubjectUtil.TYPE_SUB_STYLE);
//                    subjectService.addSubject(subject);
//                }
//                //心情场景
//                List subjects3 = JSONObject.parseArray(channels.get("scenario").toString());
//                for(Object subjectObject:subjects3){
//                    Map subjectMap = (Map) subjectObject;
//                    Subject subject = new Subject();
//                    subject.setId(subjectMap.get("id").toString());
//                    if(subjectService.get(subject.getId())!= null)continue;
//                    logger.info(subjectObject.toString());
//                    subject.setName(subjectMap.get("name").toString());
//                    subject.setDescription(subjectMap.get("intro").toString());
//                    subject.setPublishedDate(LocalDate.now());
//                    subject.setCover(subjectMap.get("cover").toString());
//                    subject.setMaster(((Map)subjectMap.get("creator")).get("name").toString());
//                    subject.setSubjectType(SubjectUtil.TYPE_MHZ);
//                    subject.setSubjectSubType(SubjectUtil.TYPE_SUB_MOOD);
//                    subjectService.addSubject(subject);
//                }
                //语言年代
                getSubjectData(JSONObject.parseArray(channels.get("language").toString()), SubjectUtil.TYPE_SUB_AGE);
                //风格流派
                getSubjectData(JSONObject.parseArray(channels.get("genre").toString()),SubjectUtil.TYPE_SUB_STYLE);
                //心情场景
                getSubjectData(JSONObject.parseArray(channels.get("scenario").toString()),SubjectUtil.TYPE_SUB_MOOD);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("爬取主题数据完成");
    }
    //执行爬取歌曲数据
    public String getSubjectSongData(String channelsId){
        String url = "https://douban.fm/j/v2/playlist?channel=" + channelsId + "&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
        String referer = "https://douban.fm/j/v2/playlist?channel=1&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
        String cookie = "flag=\"ok\"; bid=w7AcI3eV0i0; ac=\"1603643242\"; _ga=GA1.2.842490175.1603643254; _gid=GA1.2.953723139.1603643254";
        String host = "douban.fm";
        Map<String,String> headers = httpUtil.buildHeaderData(referer,cookie,host);
        String content = httpUtil.getContent(url,headers);
        Map data = JSONObject.parseObject(content,Map.class);
        if(data == null){
            logger.error("爬取歌曲失败！");
            return null;
        }
        Object o1 = null;
        if(data.containsKey("song")){
            o1 = data.get("song");
        }
        if(o1 == null){
            return "";
        }
        content = o1.toString();
        return content;
    }

    //爬取主题函数子函数
    private void getSubjectData(List subjects,String subType){
        logger.info("开始爬取"+subType+"主题数据");
        for(Object subjectObject:subjects){
            Map subjectMap = (Map) subjectObject;
            Subject subject = new Subject();
            subject.setId(subjectMap.get("id").toString());

            Subject subject1 =  subjectService.get(subject.getId());
            if(subject1!= null){
                if(subject1.getSongIds()== null || subject1.getSongIds().isEmpty()){
                    if(subject1.getSongIds() == null){
                        subject1.setSongIds(new ArrayList<>());
                    }
                    String songs = getSubjectSongData(subject1.getId());
                    addSong(subject1,songs);
                    subjectService.modifySongList(subject1);
                }
                continue;
            }
            subject.setName(subjectMap.get("name").toString());
            subject.setDescription(subjectMap.get("intro").toString());
            subject.setPublishedDate(LocalDate.now());
            subject.setCover(subjectMap.get("cover").toString());
            subject.setMaster(((Map)subjectMap.get("creator")).get("name").toString());
            subject.setSubjectType(SubjectUtil.TYPE_MHZ);
            subject.setSubjectSubType(subType);
            String songs = getSubjectSongData(subject.getId());
            addSong(subject,songs);
            subjectService.addSubject(subject);
        }
        logger.info("爬取"+subType+"主题数据完成");
    }

    //将歌曲ID加入subject
    private void addSong(Subject subject, String content){
        if(content == null || content.equals("")){
            return;
        }
        if(subject.getSongIds()==null){
            subject.setSongIds(new ArrayList<>());
        }
        List<String> list = subject.getSongIds();
        List songsObject = JSONArray.parseArray(content);
        for(Object songObject:songsObject){
            Map song = (Map) songObject;
            //添加歌手
            List singerList = JSONArray.parseArray(song.get("singers").toString());
            for(Object singerObject:singerList){
                Map singer = (Map)singerObject;
                if(singerService.get(singer.get("id").toString())!=null)continue;
                Singer singer1 = new Singer();
                singer1.setName(singer.get("name").toString());
                singer1.setId(singer.get("id").toString());
                singer1.setAvatar(singer.get("avatar").toString());
                singerService.addSinger(singer1);
            }
            //如果subject中有相同的歌曲则不添加
            if(list.contains(song.get("sid").toString()))continue;
            list.add(song.get("sid").toString());
        }
    }

    //爬取歌单
    private void getCollectionsData(){
        logger.info("开始爬取歌单");
        String url = "https://douban.fm/j/v2/songlist/explore?type=hot&genre=0&limit=20&sample_cnt=5";
        String host = "douban.fm";
        String cookie = "bid=w7AcI3eV0i0; _ga=GA1.2.842490175.1603643254; _gid=GA1.2.953723139.1603643254; flag=\"ok\"";
        String content = httpUtil.getContent(url,
                httpUtil.buildHeaderData(null,cookie,host));
        List json = JSONArray.parseArray(content);
        for(Object object:json){
            Map map = (Map)object;
            if(subjectService.get(map.get("id").toString())==null){
                Subject subject = new Subject();
                subject.setId(map.get("id").toString());
                subject.setSubjectType(SubjectUtil.TYPE_COLLECTION);
                subject.setMaster(((Map)map.get("creator"))
                        .get("id").toString());
                subject.setCover(map.get("cover").toString());
                subject.setPublishedDate(LocalDate.now());
                subject.setName(map.get("title").toString());
                subject.setDescription(map.get("intro").toString());
                subject.setSongIds(new ArrayList<>());
                List<String> songIds = subject.getSongIds();
                List sampleSongs = JSONArray.parseArray(map.get("sample_songs").toString());
                for(Object object2:sampleSongs){
                    Map map2 = (Map)object2;
                    songIds.add(map2.get("sid").toString());
                    //TODO 存入歌曲
                }
                subjectService.addSubject(subject);
            }

        }
        logger.info("爬取歌单完成");
    }


}
