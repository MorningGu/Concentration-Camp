package hero.concentrationcamp.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hero on 2016/12/8 0008.
 */
@Entity
public class Joke {
    @Id(autoincrement = true)
    private Long t_id;
    @SerializedName("id")
    private String s_id;  //server id
    private String title; //标题，只有图片和动图的时候有用
    private String img; //图片地址
    private int type; //1是文字，2是jpg图片，3是动图
    private String ct; //时间
    private String text; //文字
    //是否收藏
    private boolean isCollected;

    @Generated(hash = 513997234)
    public Joke(Long t_id, String s_id, String title, String img, int type,
            String ct, String text, boolean isCollected) {
        this.t_id = t_id;
        this.s_id = s_id;
        this.title = title;
        this.img = img;
        this.type = type;
        this.ct = ct;
        this.text = text;
        this.isCollected = isCollected;
    }

    @Generated(hash = 2009425276)
    public Joke() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}
