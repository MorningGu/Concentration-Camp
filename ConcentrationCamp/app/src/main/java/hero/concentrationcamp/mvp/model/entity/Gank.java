package hero.concentrationcamp.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

import hero.concentrationcamp.mvp.model.greendao.converter.ListStringConverter;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by hero on 2016/12/6 0006.
 */
@Entity
public class Gank implements MultiItemEntity {
    @Transient
    public static final int IMAGE = 0x01;
    @Transient
    public static final int NORMAL = 0x02;

    @Id(autoincrement = true)
    private Long t_id;//本地数据库id
    @SerializedName("_id")
    private String s_id;//server id
    private String createdAt;//"2016-12-02T22:35:51.517Z"
    private String desc;//"美团：常见性能优化策略的总结"
    private String publishedAt;//"2016-12-05T11:40:51.351Z"
    private String source;//"web"
    private String type;//"Android"
    private String who;//"solid"
    private String url;
    @Convert(converter = ListStringConverter.class,columnType = String.class)
    private List<String> images;
    private boolean used;
    //是否收藏
    private boolean isCollected;

    @Generated(hash = 178726397)
    public Gank(Long t_id, String s_id, String createdAt, String desc,
            String publishedAt, String source, String type, String who, String url,
            List<String> images, boolean used, boolean isCollected) {
        this.t_id = t_id;
        this.s_id = s_id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.who = who;
        this.url = url;
        this.images = images;
        this.used = used;
        this.isCollected = isCollected;
    }

    @Generated(hash = 116302247)
    public Gank() {
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

    public boolean getUsed() {
        return this.used;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public boolean getIsCollected() {
        return this.isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public Long getT_id() {
        return t_id;
    }

    public void setT_id(Long t_id) {
        this.t_id = t_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    @Override
    public int getItemType() {
        if("福利".equals(type)){
            return IMAGE;
        }
        return NORMAL;
    }
}
