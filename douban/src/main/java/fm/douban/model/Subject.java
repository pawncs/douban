package fm.douban.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pawncs on 2020/10/18.
 */
public class Subject {
    String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime gmtCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime gmtModified;
    String name;
    //详细说明
    String description;
    //封面图
    String cover;
    //对歌单说，是作者
    //对兆赫来说，是音乐家
    String master;
    //发布时间
    LocalDate publishedDate;
//    一级分类
    String subjectType;
//    二级分类
    String subjectSubType;
//    关联歌曲列表
    List<String> songIds;

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", gmtCreated=" + gmtCreated +
                ", gmtModified=" + gmtModified +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                ", master='" + master + '\'' +
                ", publishedDate=" + publishedDate +
                ", subjectType='" + subjectType + '\'' +
                ", subjectSubType='" + subjectSubType + '\'' +
                ", songIds=" + songIds +
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectSubType() {
        return subjectSubType;
    }

    public void setSubjectSubType(String subjectSubType) {
        this.subjectSubType = subjectSubType;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
}
