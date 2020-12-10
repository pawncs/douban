package fm.douban.service;

import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import org.springframework.data.domain.Page;

/**
 * Created by pawncs on 2020/10/15.
 */
public interface SongService {
    //添加一首歌
    Song add(Song song);
    //根据Id查询
    Song get(String songId);
    //查询全部歌曲
    Page<Song> list(SongQueryParam songParam);
    //修改一首歌
    boolean modify(Song song);
    //删除一首歌
    boolean delete(String songId);
    //删除所有
    long deleteAll();
}
