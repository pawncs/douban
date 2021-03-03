package fm.douban.service;

import fm.douban.model.Favorite;

import java.util.List;

/**
 * Created by pawncs on 2021/3/1.
 */
public interface FavoriteService {
    //添加一个喜欢
    Favorite add(Favorite fav);
    //计算喜欢数，如果大于0，表示已经喜欢
    List<Favorite> list(Favorite favParam);
    boolean delete(Favorite favParam);
}
