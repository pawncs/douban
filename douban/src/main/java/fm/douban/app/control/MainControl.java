package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawncs on 2020/10/26.
 */
@Controller
public class MainControl {
    SongService songService;
    SingerService singerService;
    SubjectService subjectService;
    Logger logger;

    @Autowired
    public MainControl(SongService songService, SingerService singerService, SubjectService subjectService) {
        this.songService = songService;
        this.singerService = singerService;
        this.subjectService = subjectService;
        this.logger = Logger.getLogger(MainControl.class);
    }


    @GetMapping(path="/index")
    public String index(Model model){
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageNum(1);
        songQueryParam.setPageSize(1);
        Song song = songService.list(songQueryParam).getContent().get(0);
        logger.info("song添加成功");
        model.addAttribute("song",song);
        List<String> singerIds = song.getSingerIds();
        List<Singer> singers = new ArrayList<>();
        for(String id:singerIds){
            Singer singer = singerService.get(id);
            if(singer != null){
                singers.add(singer);
            }

        }
        logger.info("singers添加成功");
        model.addAttribute("singers",singers);
        return "index";
    }
}
