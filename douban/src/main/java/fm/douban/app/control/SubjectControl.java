package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawncs on 2021/2/4.
 */
@Controller
public class SubjectControl {
    SubjectService subjectService;
    SongService songService;
    SingerService singerService;
    Logger logger;

    @Autowired
    public SubjectControl(SubjectService s, SongService songService, SingerService singerService) {
        this.subjectService = s;
        this.songService = songService;
        this.singerService = singerService;
        this.logger = Logger.getLogger(SubjectControl.class);
    }

    //艺术家详情
    @GetMapping(path = "/artist")
    public String mhzDetail(Model model, @RequestParam(name = "subjectId") String subjectId) {
        //传递subject给模板
        Subject s = subjectService.get(subjectId);
        model.addAttribute("subject", s);
        //传递songs给模板
        List<String> songIdList = s==null?new ArrayList<>():s.getSongIds();
        List<Song> songs = new ArrayList<>();
        for (String ids : songIdList) {
            Song song = songService.get(ids);
            songs.add(song);
        }
        model.addAttribute("songs", songs);

        //传递主题关联歌手
        String songId = s==null?"":s.getSongIds().get(0);
        Song song = songService.get(songId);
        String singerId = song==null?"":song.getSingerIds().get(0);
        Singer singer = singerService.get(singerId);
        model.addAttribute("singer", singer==null?"":singer);

        //传递相似歌手列表
        List<String> singerList = singer==null?null:singer.getSimilarSingerIds();
        List<Singer> simSingers = new ArrayList<>();
        if(singerList!=null){
            for(String s1:singerList){
                simSingers.add(singerService.get(s1));
            }
        }

        model.addAttribute("simSingers",simSingers);
        return "mhzdetail";
    }

    //歌单列表
    @GetMapping(path = "/collection")
    String collection(Model model){
        //todo 起个头
        return null;
    }

}
