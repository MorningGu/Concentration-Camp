package hero.concentrationcamp.mvp.model.entity;

/**
 * Created by hero on 2016/11/30 0030.
 * 资源中的栏目
 */

public class SourceColumn {

    String name;//名字
    String code;//请求中用的字段名
    public SourceColumn(String name,String code){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
