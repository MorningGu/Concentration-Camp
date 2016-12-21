package hero.concentrationcamp.mvp.contract;

import java.util.List;

import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;

/**
 * Created by hero on 2016/12/2 0002.
 */

public interface GankSubContract {
    interface IGankSubFragmentView{
        void updateData(boolean isRefresh, List<Gank> data);//数据请求
        void updateItemState(int position);//单个item的收藏状态改变后，在这里进行更新
        void updateData(Gank gank);//收藏状态在其他页面变更后，在这里要进行更新
    }
    interface IGankSubFragmentPresenter{
        void getGankData(SourceColumn column,int pageNo,boolean isRefresh);
        void setCollectState(int position,Gank gank);
    }
}
