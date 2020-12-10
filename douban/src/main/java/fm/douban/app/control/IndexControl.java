package fm.douban.app.control;

import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pawncs on 2020/10/16.
 */
@Controller
public class IndexControl {
    SubjectService subjectService;
    SingerService singerService;
    SongService songService;
    @Autowired
    public IndexControl(SubjectService subjectService, SingerService singerService, SongService songService) {
        this.subjectService = subjectService;
        this.singerService = singerService;
        this.songService = songService;
    }
    @RequestMapping("index")
    public String index(){
        Logger log = Logger.getLogger(IndexControl.class);
        log.info("访问index");
        return "index";
    }
    @RequestMapping("delete")
    @ResponseBody
    public String deleteAll(){
        long s1 = singerService.deleteAll();
        long s2 = songService.deleteAll();
        long s3 = subjectService.deleteAll();
        return "singer:"+s1+" song:"+s2+"subject:"+s3;
    }

//    @ResponseBody
//    @RequestMapping("/test/singer/add")
//    public String testAddSinger(){
//        Singer singer = new Singer();
//        singer.setName("sada");
//        return JSON.toJSONString(singerService.addSinger(singer));
//    }
}