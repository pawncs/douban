package fm.douban.app.control;

import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pawncs on 2020/10/17.
 */
@RestController
@RequestMapping("/test/song")
public class SongTestControl {
    SongService songService;
    @Autowired
    public SongTestControl(SongService songService) {
        this.songService = songService;
    }
    @RequestMapping("/add")
    public Song testAdd(){
        Song song = new Song();
        song.setId("0");
        return songService.add(song);
    }
    @RequestMapping(value = "/get")
    public Song testGet(@RequestParam("id")String id){
        return songService.get(id);
    }
    @RequestMapping("list")
    public Page<Song> testList(){
        return songService.list(new SongQueryParam());
    }
    @RequestMapping("modify")
    public boolean testModify(){
        Song song = new Song();
        song.setId("0");
        song.setName("BOBO");
        return songService.modify(song);
    }
    @RequestMapping("del")
    public boolean testDelete(){
        return songService.delete("0");
    }
}
