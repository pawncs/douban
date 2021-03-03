package fm.douban.app.control;

import fm.douban.model.*;
import fm.douban.param.SongQueryParam;
import fm.douban.service.FavoriteService;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.FavoriteUtil;
import fm.douban.util.SubjectUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pawncs on 2020/10/26.
 */
@Controller
public class MainControl {
    SongService songService;
    SingerService singerService;
    SubjectService subjectService;
    FavoriteService favoriteService;
    Logger logger;

    @Autowired
    public MainControl(SongService songService, SingerService singerService, SubjectService subjectService,
                       FavoriteService favoriteService) {
        this.songService = songService;
        this.singerService = singerService;
        this.subjectService = subjectService;
        this.favoriteService = favoriteService;
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
                    //logger.error("subjuctSubType错误");
                    //break;
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

    @GetMapping(path = "/search")
    public String search(Model model) {
        //todo
        return "search";
    }

    @GetMapping(path = "searchContent")
    @ResponseBody
    public Map searchContent(@RequestParam(name = "keyword") String keyword) {
        Map<String, Page<Song>> map = new HashMap<>();
        Page<Song> songList;
        SongQueryParam songQueryParam = new SongQueryParam();
        //List<Song>  allSongs = songService.list(songQueryParam).getContent();
        songQueryParam.setName(keyword);
        //System.out.println(".,."+allSongs.get(0).getName());
        songList = songService.list(songQueryParam);

        map.put("songs", songList);
        //System.out.println(map.get("songs").getContent().toString());
        return map;
    }

    //我的页面
    @GetMapping(path = "/my")
    public String myPage(Model model,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        Favorite favorite = new Favorite();
        favorite.setType(FavoriteUtil.TYPE_RED_HEART);
        favorite.setUserId(((UserLoginInfo)request.getSession().getAttribute("userLoginInfo")).getUserId());
        List<Favorite> list = favoriteService.list(favorite);
        model.addAttribute("favorites",list);
        //没懂，查询已经点了红心的歌曲是这样吗
        List<Favorite> songs = new ArrayList<>();
        for(Favorite f:list){
            if(f.getItemType().equals(FavoriteUtil.ITEM_TYPE_SONG)){
                songs.add(f);
            }
        }
        model.addAttribute("songs",songs);
        return "my";
    }

    /**
     * 喜欢或不喜欢操作
     * 已经喜欢，则删除，表示执行不喜欢操作
     * 还没有喜欢则记录，则新增，表示执行喜欢操作
     */
    @GetMapping(path = "/fav")
    @ResponseBody
    public Map doFav(@RequestParam(name = "itemType") String itemType,
                     @RequestParam(name = "itemId") String itemId,
                     HttpServletRequest request,
                     HttpServletResponse response) {
        Map returnData = new HashMap();
        Favorite favorite = new Favorite();
        favorite.setItemId(itemId);
        favorite.setItemType(itemType);
        favorite.setType(FavoriteUtil.TYPE_RED_HEART);
        favorite.setUserId(((UserLoginInfo)request.getSession().getAttribute("userLoginInfo")).getUserId());
        if(!favoriteService.delete(favorite)){
            favoriteService.add(favorite);
        }
        returnData.put("message","successful");
        return returnData;
    }
}
