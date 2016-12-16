package hero.concentrationcamp.mvp.presenter;

import java.util.List;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.IBaseView;
import hero.concentrationcamp.mvp.contract.GankSubContract;
import hero.concentrationcamp.mvp.data.GankSubFragmentDataFactory;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.GankDao;
import hero.concentrationcamp.mvp.model.entity.GankVo;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.model.greendao.GreenDaoHelper;
import hero.concentrationcamp.retrofit.ApiManager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankSubFragmentPresenter extends BasePresenter<GankSubContract.IGankSubFragmentView> implements GankSubContract.IGankSubFragmentPresenter {
    GankSubFragmentDataFactory factory = new GankSubFragmentDataFactory();
    @Override
    public void getGankData(SourceColumn column, int pageNo, final boolean isRefresh) {
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<GankVo>() {
            @Override
            public void onNext(GankVo vo) {
                GankSubContract.IGankSubFragmentView view  = getView();
                if(vo.isError()){
                    //// TODO: 2016/12/6 0006 提示服务器错误
                    if(view instanceof IBaseView){
                        ((IBaseView)view).showError("服务器错误");
                    }
                    return;
                }
                view.updateData(isRefresh,vo.getResults());
            }

            @Override
            public void onError(Throwable t) {
                //// TODO: 2016/12/6 0006 提示网络错误
                GankSubContract.IGankSubFragmentView view  = getView();
                if(view instanceof IBaseView){
                    ((IBaseView)view).showError("网络错误");
                }
                t.printStackTrace();
                dispose();
            }

            @Override
            public void onComplete() {
                dispose();
            }
        };
        addSubscription(factory.getDataList(resultSubscriber,column,pageNo));
    }

    /**
     * 获取本地收藏的数据
     * @param isRefresh
     * @param tid
     */
    public void getGankData(final boolean isRefresh,Long tid){
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<List<Gank>>() {
            @Override
            public void onNext(List<Gank> ganks) {
                GankSubContract.IGankSubFragmentView view  = getView();
                if(view!=null){
                    view.updateData(isRefresh,ganks);
                }
            }

            @Override
            public void onError(Throwable t) {
                GankSubContract.IGankSubFragmentView view  = getView();
                if(view instanceof IBaseView){
                    ((IBaseView)view).showError("加载异常");
                }
                t.printStackTrace();
                dispose();
            }

            @Override
            public void onComplete() {
                dispose();
            }
        };
        addSubscription(factory.getDataList(resultSubscriber,isRefresh,tid,10));
    }
    @Override
    public void setCollectState(final int position, final Gank gank) {
        Flowable flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                if(gank.isCollected()){
                    //已经收藏过，取消收藏
                    gank.setCollected(false);
                    GreenDaoHelper.getDaoSession().getGankDao().queryBuilder()
                            .where(GankDao.Properties.S_id.eq(gank.getS_id()))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                }else{
                    //还没收藏，添加收藏
                    gank.setCollected(true);
                    GreenDaoHelper.getDaoSession().getGankDao().insert(gank);
                }
                e.onNext(0);
                e.onComplete();
            }
        }, BackpressureStrategy.DROP);
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<Integer>() {
            @Override
            public void onNext(Integer integer) {
                //更新ui
                GankSubContract.IGankSubFragmentView view  = getView();
                if(view==null)
                    return;
                view.updateItemState(position);
            }

            @Override
            public void onError(Throwable t) {
                dispose();
            }

            @Override
            public void onComplete() {
                dispose();
            }
        };
        addSubscription(ApiManager.INSTANCE.startObservable(flowable,resultSubscriber));
    }
}
