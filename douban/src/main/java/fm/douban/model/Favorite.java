package fm.douban.model;

import java.time.LocalDateTime;

/**
 * Created by pawncs on 2021/3/1.
 */
public class Favorite {
    private String id;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
    private String userId;
    //喜欢的类型：红心/收藏
    private String type;
    //喜欢的目标，歌曲歌手mhz等
    private String itemType;
    //喜欢的目标ID
    private String itemId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
