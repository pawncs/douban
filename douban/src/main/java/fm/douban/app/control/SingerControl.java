package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawncs on 2021/1/28.
 */
@Controller
public class SingerControl {
    SingerService singerService;
    Logger logger;
    @Autowired
    public SingerControl(SingerService singerService){
        this.singerService = singerService;
        this.logger = Logger.getLogger(SingerControl.class);
    }
    @GetMapping("/user-guide")
    public String myMhz(Model model){
        List<Singer> singers = randomSingers();
        if(singers.isEmpty()){
            logger.error("singers为空！");
        }
//        else{
//            logger.info(singers.toString());
//        }
        model.addAttribute("singers",singers);
        return "userguide";
    }
    @GetMapping("/singer/random")
    @ResponseBody
    List<Singer> randomSingers(){//返回十个歌手
        List<Singer> AllSingers = singerService.getAll();
        List<Singer> singers = new ArrayList<>();
        int num = AllSingers.size();
        List<Integer> indexes = new ArrayList<>();
        int i = 0;
        while(i < 10){
            int index = (int)(Math.random()*num);
            if(!indexes.contains(index)){
                indexes.add(index);
                i++;
                logger.info(i);
            }
        }
        for(int index:indexes){
            singers.add(AllSingers.get(index));
        }
        return singers;
    }
}
