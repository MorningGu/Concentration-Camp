package hero.concentrationcamp.mvp.presenter;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.contract.GankSubContract;
import hero.concentrationcamp.mvp.data.GankSubFragmentDataFactory;
import hero.concentrationcamp.mvp.model.entity.GankVo;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.retrofit.ApiManager;
import hero.concentrationcamp.ui.gank.GankSubFragment;
import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankSubFragmentPresenter extends BasePresenter<GankSubFragment> implements GankSubContract.IGankSubFragmentPresenter {
    GankSubFragmentDataFactory factory = new GankSubFragmentDataFactory();
    @Override
    public void getGankData(SourceColumn column, int pageNo, final boolean isRefresh) {
        Flowable<GankVo> flowable = factory.getDataList(column,pageNo);
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<GankVo>() {
            @Override
            public void onNext(GankVo vo) {
                GankSubFragment view  = getView();
                if(vo.isError()){
                    //// TODO: 2016/12/6 0006 提示服务器错误
                    view.showError("服务器错误");
                    return;
                }
                if(vo.getResults()==null || vo.getResults().size()==0){
                    //// TODO: 2016/12/6 0006  提示没数据 
                    view.showError("没有数据了");
                    return;
                }
                view.updateData(isRefresh,vo.getResults());
            }

            @Override
            public void onError(Throwable t) {
                //// TODO: 2016/12/6 0006 提示网络错误
                GankSubFragment view  = getView();
                view.showError("网络错误");
                t.printStackTrace();
                dispose();
                return;
            }

            @Override
            public void onComplete() {
                dispose();
            }
        };
        addSubscription(ApiManager.INSTANCE.startObservable(flowable,resultSubscriber));
    }
}
