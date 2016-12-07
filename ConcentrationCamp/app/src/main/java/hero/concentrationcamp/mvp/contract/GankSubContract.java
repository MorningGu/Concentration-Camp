package hero.concentrationcamp.mvp.contract;

import java.util.List;

import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;

/**
 * Created by hero on 2016/12/2 0002.
 */

public interface GankSubContract {
    interface IGankSubFragmentView{
        void updateData(boolean isRefresh, List<Gank> data);
    }
    interface IGankSubFragmentPresenter{
        void getGankData(SourceColumn column,int pageNo,boolean isRefresh);
    }
}
