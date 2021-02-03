package fm.douban.app.control;

import fm.douban.model.MhzViewModel;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
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

    @GetMapping("")
    public String index2(Model model) {
        return index(model);
    }

    @GetMapping(path = "/index")
    public String index(Model model) {
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageNum(1);
        songQueryParam.setPageSize(1);
        Song song = songService.list(songQueryParam).getContent().get(0);
        logger.info("song添加成功");
        model.addAttribute("song", song);
        List<String> singerIds = song.getSingerIds();
        List<Singer> singers = new ArrayList<>();
        for (String id : singerIds) {
            Singer singer = singerService.get(id);
            if (singer != null) {
                singers.add(singer);
            }

        }
        logger.info("singers添加成功");
        model.addAttribute("singers", singers);

        List<Singer> artistDatas = new ArrayList<>();
        List<MhzViewModel> mhzViewModels = new ArrayList<>();

        MhzViewModel mood = new MhzViewModel();
        mood.setTitle("心情/场景");
        List<Subject> moodList = new ArrayList<>();
        mood.setSubjects(moodList);
        mhzViewModels.add(mood);

        MhzViewModel age = new MhzViewModel();
        age.setTitle("语言/年代");
        List<Subject> ageList = new ArrayList<>();
        age.setSubjects(ageList);
        mhzViewModels.add(age);

        MhzViewModel style = new MhzViewModel();
        style.setTitle("风格/流派");
        List<Subject> styleList = new ArrayList<>();
        style.setSubjects(styleList);
        mhzViewModels.add(style);

        for (Subject subject : subjectService.getSubjects()) {
            if (!subject.getSubjectType().equals(SubjectUtil.TYPE_MHZ)) {
                continue;
            }
            //logger.info("添加" + subject.getSubjectSubType());
            switch (subject.getSubjectSubType()) {
                case SubjectUtil.TYPE_SUB_AGE:
                    ageList.add(subject);
                    break;
                case SubjectUtil.TYPE_SUB_MOOD:
                    moodList.add(subject);
                    break;
                case SubjectUtil.TYPE_SUB_STYLE:
                    styleList.add(subject);
                    break;
//                case SubjectUtil.TYPE_SUB_ARTIST:
//                    artistDatas.add(subject);
//                    logger.info(subject.toString());
//                    break;
                default:
                    logger.error("subjuctSubType错误");
                    break;
            }
        }
        List<Singer> list = singerService.getAll();
        for (int i = 0; i < 10; i++) {
            Singer s = list.get(i);
            artistDatas.add(list.get(i));
        }

        model.addAttribute("artistDatas", artistDatas);
        if (artistDatas.size() == 0) {
            logger.info("QAQ");
        }
        model.addAttribute("mhzViewModels", mhzViewModels);

        return "index";
    }
}
