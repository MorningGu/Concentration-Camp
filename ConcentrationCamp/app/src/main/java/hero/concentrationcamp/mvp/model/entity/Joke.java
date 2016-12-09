package hero.concentrationcamp.mvp.model.entity;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class Joke {
    private String id;
    private String title; //标题，只有图片和动图的时候有用
    private String img; //图片地址
    private int type; //1是文字，2是jpg图片，3是动图
    private String ct; //时间
    private String text; //文字

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
