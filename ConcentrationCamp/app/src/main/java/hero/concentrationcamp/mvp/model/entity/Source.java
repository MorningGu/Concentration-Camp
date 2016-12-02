package hero.concentrationcamp.mvp.model.entity;

import java.util.List;

/**
 * Created by hero on 2016/11/30 0030.
 * 资源
 */

public class Source {
    String name;//名字
    int code; //资源的代号 0 gank/1 段子/2 美拍
    List<SourceColumn> columns;//资源的栏目

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<SourceColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<SourceColumn> columns) {
        this.columns = columns;
    }
}
