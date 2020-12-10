package fm.douban.param;

import fm.douban.model.Song;

/**
 * Created by pawncs on 2020/10/15.
 */
public class SongQueryParam extends Song {
    private int pageNum = 1;
    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
