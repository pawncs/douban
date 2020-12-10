package fm.douban.service;

import fm.douban.model.Singer;

import java.util.List;

/**
 * Created by pawncs on 2020/10/15.
 */
public interface SingerService {
    //增加歌手
    Singer addSinger(Singer singer);
    //根据id查询歌手
    Singer get(String singerId);
    //查询全部歌手
    List<Singer> getAll();
    //修改歌手，不能修改主键和创建时间
    boolean modify(Singer singer);
    //根据ID删除歌手
    boolean delete(String singerId);
    //删除所有
    long deleteAll();
}
