package fm.douban.spider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import fm.douban.util.JSONUtil;
import fm.douban.util.SubjectUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    SongService songService;
    Logger logger;
    HttpUtil httpUtil;

    @Autowired
    public SubjectSpider(SubjectService subjectService, SingerService singerService,SongService songService, HttpUtil httpUtil) {
        this.subjectService = subjectService;
        this.songService = songService;
        this.singerService = singerService;
        logger = Logger.getLogger(SubjectSpider.class);
        this.httpUtil = httpUtil;
    }

    //系统启动的时候自动执行爬取任务
   //@PostConstruct
    public void init() {
        doExcute();
    }

    //开始爬取任务
    public void doExcute() {
        getSubjectData();
        getCollectionsData();
    }

    //执行爬取主题数据
    private void getSubjectData() {
        logger.info("开始爬取主题数据");
        try {
            String urlString = "https://fm.douban.com/j/v2/rec_channels?specific=all";

            String host = "fm.douban.com";
            String cookie = "ll=\"118159\"; bid=DutGmrhEVeY; douban-fav-remind=1; _ga=GA1.2.159710958.1603614066; __gads=ID=267e6b130124c008-22d5bb7168c40043:T=1603718991:RT=1603718991:S=ALNI_MbBSnjSqeA7oiYyWiOL4_iOsTKIjg; _vwo_uuid_v2=D2C1C69B98BDE7695C303D232C07FBB52|2d0db08722b6c02cd8023defa0f645e2; gr_user_id=78e672f6-9e74-4600-b714-fede39aa5dcd; viewed=\"2130190_34988786_1405212\"; __utma=30149280.1301095399.1601823804.1608516668.1611211198.8; __utmz=30149280.1611211198.8.7.utmcsr=so.com|utmccn=(referral)|utmcmd=referral|utmcct=/link";
            String content = httpUtil.getContent(urlString,
                    httpUtil.buildHeaderData(null, cookie, host));

//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//            InputStream is;
//            if (connection.getResponseCode() == 200) {
            if(!content.equals("")){

            logger.info("进入if");
//                is = connection.getInputStream();
//                BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                StringBuffer sbf = new StringBuffer();
//                String temp = null;
//                while ((temp = bf.readLine()) != null) {
//                    sbf.append(temp);
//                    sbf.append("\r\n");
//                }
                //Map data = (Map) JSONObject.parseObject(sbf.toString(), Map.class).get("data");
                Map data = (Map) JSONObject.parseObject(content, Map.class).get("data");
                logger.info("开始判断非空");
                if (data == null) {
                    logger.error("不存在data!");
                    return;
                }
                Map channels = (Map) data.get("channels");
                if (channels == null) {
                    logger.error("不存在channels");
                    return;
                }
                logger.info("判断非空完成");
                //歌手
                List artists = JSONObject.parseObject(channels.get("artist").toString(), List.class);
                for (Object artistObject : artists) {
                    Map artist = (Map) artistObject;
                    Singer singer = new Singer();
                    singer.setId(artist.get("artist_id").toString());
                    singer.setAvatar(artist.get("cover").toString());
                    if (singerService.get(singer.getId()) != null) continue;
                    List relatedArtists = JSONObject.parseObject(artist.get("related_artists").toString(), List.class);
                    Map map = (Map) (relatedArtists.get(0));
                    singer.setName(map.get("name").toString());
                    if (singer.getSimilarSingerIds() == null) {
                        singer.setSimilarSingerIds(new ArrayList<>());
                    }
                    List similar = singer.getSimilarSingerIds();
                    for (Object relatedArtistsObject : relatedArtists) {
                        Map relatedArtist = (Map) relatedArtistsObject;
                        similar.add(relatedArtist.get("id").toString());
                    }
                    singerService.addSinger(singer);
                }
                logger.info("开始进入副函数");
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
                //艺术家
                getSubjectData(JSONObject.parseArray(channels.get("artist").toString()), SubjectUtil.TYPE_SUB_ARTIST);
                //语言年代
                getSubjectData(JSONObject.parseArray(channels.get("language").toString()), SubjectUtil.TYPE_SUB_AGE);
                //风格流派
                getSubjectData(JSONObject.parseArray(channels.get("genre").toString()), SubjectUtil.TYPE_SUB_STYLE);
                //心情场景
                getSubjectData(JSONObject.parseArray(channels.get("scenario").toString()), SubjectUtil.TYPE_SUB_MOOD);
                logger.info("爬取三大风格主题数据完成");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("爬取主题数据完成");
    }

    //执行爬取歌曲数据
    public String getSubjectSongData(String channelsId) {
        //logger.info("爬取歌曲" + "getSubjectSongData");
//        String url = "https://douban.fm/j/v2/playlist?channel=" + channelsId + "&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
//        String referer = "https://douban.fm/j/v2/playlist?channel=1&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
//        String cookie = "flag=\"ok\"; bid=w7AcI3eV0i0; ac=\"1603643242\"; _ga=GA1.2.842490175.1603643254; _gid=GA1.2.953723139.1603643254";
//        String host = "douban.fm";
        String url = "https://fm.douban.com/j/v2/playlist?channel=" + channelsId + "&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
        String referer = "https://fm.douban.com/j/v2/playlist?channel=1&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
        String cookie = "ll=\"118159\"; bid=DutGmrhEVeY; douban-fav-remind=1; _ga=GA1.2.159710958.1603614066; __gads=ID=267e6b130124c008-22d5bb7168c40043:T=1603718991:RT=1603718991:S=ALNI_MbBSnjSqeA7oiYyWiOL4_iOsTKIjg; _vwo_uuid_v2=D2C1C69B98BDE7695C303D232C07FBB52|2d0db08722b6c02cd8023defa0f645e2; gr_user_id=78e672f6-9e74-4600-b714-fede39aa5dcd; viewed=\"2130190_34988786_1405212\"; __utma=30149280.1301095399.1601823804.1608516668.1611211198.8; __utmz=30149280.1611211198.8.7.utmcsr=so.com|utmccn=(referral)|utmcmd=referral|utmcct=/link";
        String host = "fm.douban.com";
        Map<String, String> headers = httpUtil.buildHeaderData(referer, cookie, host);
        String content = httpUtil.getContent(url, headers);
        Map data = JSONObject.parseObject(content, Map.class);
        if (data == null) {
            logger.error("爬取歌曲失败！");
            return null;
        }
        Object o1 = null;
        if (data.containsKey("song")) {
            o1 = data.get("song");
        }
        if (o1 == null) {
            return "";
        }
        content = o1.toString();
        //logger.info("爬取歌曲" + content);

        return content;
    }

    //爬取主题函数子函数
    private void getSubjectData(List subjects, String subType) {
        logger.info("开始爬取" + subType + "主题数据");
        for (Object subjectObject : subjects) {
            Map subjectMap = (Map) subjectObject;
            //logger.info("map:" + subjectMap.toString());
            Subject subject = new Subject();
            subject.setId(subjectMap.get("id").toString());

            Subject subject1 = subjectService.get(subject.getId());
            //logger.info(subject1.toString());
            if (subject1 != null) {
                if (subject1.getSongIds() == null) {
                    subject1.setSongIds(new ArrayList<>());
                }
                String songs = getSubjectSongData(subject1.getId());
                addSong(subject1, songs);
                subjectService.modifySongList(subject1);

                continue;
            }
            subject.setName(subjectMap.get("name").toString());
            subject.setDescription(subjectMap.get("intro").toString());
            subject.setPublishedDate(LocalDate.now());
            subject.setCover(subjectMap.get("cover").toString());
            subject.setMaster(((Map) subjectMap.get("creator")).get("name").toString());
            subject.setSubjectType(SubjectUtil.TYPE_MHZ);
            subject.setSubjectSubType(subType);

            String songs = getSubjectSongData(""+subjectMap.get("id"));
            addSong(subject, songs);
//            //todo 添加专辑歌曲
//            List list = (List) subjectMap.get("songIds");
//            logger.info(list.toString());
//            for(int i = 0;i<list.size();i++){
//                String songs = getSubjectSongData((String) list.get(i));
//                addSong(subject, songs);
//            }

            subjectService.addSubject(subject);
        }
        logger.info("爬取" + subType + "主题数据完成");
    }

    //将歌曲ID加入subject
    private void addSong(Subject subject, String content) {
        if(subject.getId().equals("39213186")){
            logger.info(content);
        }
        //logger.info("添加歌曲" + content);
        if (content == null || content.equals("")) {
            return;
        }
        if (subject.getSongIds() == null) {
            subject.setSongIds(new ArrayList<>());
        }
        List<String> list = subject.getSongIds();
        List songsObject = JSONArray.parseArray(content);
        for (Object songObject : songsObject) {
            Song realSong = JSONUtil.json2song(songObject.toString());
            if(songService.get(realSong.getId()) == null){
                songService.add(realSong);
            }

            Map song = (Map) songObject;
            //添加歌手
            List singerList = JSONArray.parseArray(song.get("singers").toString());
            for (Object singerObject : singerList) {
                Map singer = (Map) singerObject;
                if (singerService.get(singer.get("id").toString()) != null) continue;
                Singer singer1 = new Singer();
                singer1.setName(singer.get("name").toString());
                singer1.setId(singer.get("id").toString());
                singer1.setAvatar(singer.get("avatar").toString());
                //logger.info("singer:"+singer1.toString());
                singerService.addSinger(singer1);
            }
            //如果subject中有相同的歌曲则不添加
            if (list.contains(song.get("sid").toString())) continue;
            list.add(song.get("sid").toString());
        }
    }

    //爬取歌单
    private void getCollectionsData() {
        logger.info("开始爬取歌单");
        String url = "https://douban.fm/j/v2/songlist/explore?type=hot&genre=0&limit=20&sample_cnt=5";
//        String host = "douban.fm";
//        String cookie = "bid=w7AcI3eV0i0; _ga=GA1.2.842490175.1603643254; _gid=GA1.2.953723139.1603643254; flag=\"ok\"";
        String host = "fm.douban.com";
        String cookie = "ll=\"118159\"; bid=DutGmrhEVeY; douban-fav-remind=1; _ga=GA1.2.159710958.1603614066; __gads=ID=267e6b130124c008-22d5bb7168c40043:T=1603718991:RT=1603718991:S=ALNI_MbBSnjSqeA7oiYyWiOL4_iOsTKIjg; _vwo_uuid_v2=D2C1C69B98BDE7695C303D232C07FBB52|2d0db08722b6c02cd8023defa0f645e2; gr_user_id=78e672f6-9e74-4600-b714-fede39aa5dcd; viewed=\"2130190_34988786_1405212\"; __utma=30149280.1301095399.1601823804.1608516668.1611211198.8; __utmz=30149280.1611211198.8.7.utmcsr=so.com|utmccn=(referral)|utmcmd=referral|utmcct=/link";
        String content = httpUtil.getContent(url,
                httpUtil.buildHeaderData(null, cookie, host));
        List json = JSONArray.parseArray(content);
        for (Object object : json) {
            Map map = (Map) object;
            if (subjectService.get(map.get("id").toString()) == null) {
                Subject subject = new Subject();
                subject.setId(map.get("id").toString());
                subject.setSubjectType(SubjectUtil.TYPE_COLLECTION);
                subject.setMaster(((Map) map.get("creator"))
                        .get("id").toString());
                subject.setCover(map.get("cover").toString());
                subject.setPublishedDate(LocalDate.now());
                subject.setName(map.get("title").toString());
                subject.setDescription(map.get("intro").toString());
//                subject.setSongIds(new ArrayList<>());
//                List<String> songIds = subject.getSongIds();
//                List sampleSongs = JSONArray.parseArray(map.get("sample_songs").toString());
//                for (Object object2 : sampleSongs) {
//                    Map map2 = (Map) object2;
//                    songIds.add(map2.get("sid").toString());
//                    //TODO 存入歌曲
//                }
                String s = getSubjectSongData(subject.getId());
                if(subject.getId().equals("39213186")){
                    logger.info(s);
                }
                addSong(subject,s);
                subjectService.addSubject(subject);
            }

        }
        logger.info("爬取歌单完成");
    }


}
