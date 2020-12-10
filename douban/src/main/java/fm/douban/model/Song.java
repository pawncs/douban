package fm.douban.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pawncs on 2020/10/15.
 */
public class Song {
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    private String name;
    //歌词
    private String lyrics;
    //封面图
    private String cover;
    //播放地址
    private String url;
    //所属歌手ID
    private List<String> singerIds;

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", gmtCreated=" + gmtCreated +
                ", gmtModified=" + gmtModified +
                ", name='" + name + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", cover='" + cover + '\'' +
                ", url='" + url + '\'' +
                ", singerIds=" + singerIds +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(LocalDateTime gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSingerIds() {
        return singerIds;
    }

    public void setSingerIds(List<String> singerIds) {
        this.singerIds = singerIds;
    }
}
