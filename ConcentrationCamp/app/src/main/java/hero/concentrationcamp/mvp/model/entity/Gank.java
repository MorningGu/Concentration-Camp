package hero.concentrationcamp.mvp.model.entity;

import java.util.List;

/**
 * Created by hero on 2016/12/6 0006.
 */

public class Gank {
    private String _id; //"584186c7421aa939b58d31cd"
    private String createdAt;//"2016-12-02T22:35:51.517Z"
    private String desc;//"美团：常见性能优化策略的总结"
    private String publishedAt;//"2016-12-05T11:40:51.351Z"
    private String source;//"web"
    private String type;//"Android"
    private String who;//"solid"
    private String url;
    private List<String> images;
    private boolean used;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
