package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by pawncs on 2020/10/15.
 */
@RestController
@RequestMapping("/test/singer")
public class SingerTestControl {
    private final SingerService singerService;
    @Autowired
    public SingerTestControl(SingerService singerService){
        this.singerService = singerService;
    }
    @RequestMapping("/add")
    Singer testAddSinger(){
        Singer singer = new Singer();
        singer.setId("0");
        singer.setName("BOBO");
        return singerService.addSinger(singer);
    }
    @RequestMapping("/getAll")
    List<Singer> testGetAll(){
        return singerService.getAll();
    }
    @RequestMapping("/getOne")
    Singer testGetSinger(){
        return singerService.get("0");
    }
    @RequestMapping("/get")
    Singer testGetSingerById(@RequestParam("id")String id){
        return singerService.get(id);
    }
    @RequestMapping("/modify")
    boolean testModifySinger(){
        Singer singer = new Singer();
        singer.setId("0");
        singer.setName("QQQ");
        return singerService.modify(singer);
    }
    @RequestMapping("/del")
    boolean testDelSinger(){
        return singerService.delete("0");
    }
}
