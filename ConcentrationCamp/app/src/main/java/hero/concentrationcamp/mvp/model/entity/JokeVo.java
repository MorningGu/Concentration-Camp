package hero.concentrationcamp.mvp.model.entity;

import java.util.List;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeVo<E> {
    private int allPages; //总页数
    private int ret_code;
    private List<E> contentlist;
    private int currentPage; //当前页数
    private int allNum; //数据的总条数
    private int maxResult; //一次请求的最大数据量

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<E> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<E> contentlist) {
        this.contentlist = contentlist;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }
}
