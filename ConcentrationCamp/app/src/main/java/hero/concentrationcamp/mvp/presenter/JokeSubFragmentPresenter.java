package hero.concentrationcamp.mvp.presenter;

import java.util.List;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.IBaseView;
import hero.concentrationcamp.mvp.contract.JokeSubContract;
import hero.concentrationcamp.mvp.data.JokeSubFragmentDataFactory;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.JokeDao;
import hero.concentrationcamp.mvp.model.entity.YiYuanResult;
import hero.concentrationcamp.mvp.model.greendao.GreenDaoHelper;
import hero.concentrationcamp.retrofit.ApiManager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeSubFragmentPresenter extends BasePresenter<JokeSubContract.IJokeSubFragmentView> implements JokeSubContract.IJokeSubFragmentPresenter {
    JokeSubFragmentDataFactory factory = new JokeSubFragmentDataFactory();

    @Override
    public void getJokeData(String type, int pageNo, final boolean isRefresh) {
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<YiYuanResult>() {
            @Override
            public void onNext(YiYuanResult vo) {
                JokeSubContract.IJokeSubFragmentView view  = getView();
                if(vo.getShowapi_res_code()!=0){
                    //// TODO: 2016/12/6 0006 提示服务器错误
                    if(view instanceof IBaseView){
                        ((IBaseView)view).showError("服务器错误");
                    }
                    return;
                }
                if(vo.getShowapi_res_body()==null){
                    //// TODO: 2016/12/6 0006  提示没数据
                    if(view instanceof IBaseView){
                        ((IBaseView)view).showError("没有数据了");
                    }
                    return;
                }
                view.updateData(isRefresh,vo.getShowapi_res_body().getContentlist());
            }

            @Override
            public void onError(Throwable t) {
                //// TODO: 2016/12/6 0006 提示网络错误
                JokeSubContract.IJokeSubFragmentView view  = getView();
                if(view instanceof IBaseView){
                    ((IBaseView)view).showError("网络错误");
                }
                t.printStackTrace();
                dispose();
                return;
            }

            @Override
            public void onComplete() {
                dispose();
            }
        };
        addSubscription(factory.getDataList(resultSubscriber,type,pageNo));
    }
    /**
     * 获取本地收藏的数据
     * @param isRefresh
     * @param tid
     */
    public void getGankData(final boolean isRefresh,Long tid){
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<List<Joke>>() {
            @Override
            public void onNext(List<Joke> jokes) {
                JokeSubContract.IJokeSubFragmentView view  = getView();
                if(view!=null){
                    view.updateData(isRefresh,jokes);
                }
            }

            @Override
            public void onError(Throwable t) {
                JokeSubContract.IJokeSubFragmentView view  = getView();
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
    public void setCollectState(final int position, final Joke joke) {
        Flowable flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                if(joke.isCollected()){
                    //已经收藏过，取消收藏
                    joke.setCollected(false);
                    GreenDaoHelper.getDaoSession().getJokeDao().queryBuilder()
                            .where(JokeDao.Properties.S_id.eq(joke.getS_id()))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                }else{
                    //还没收藏，添加收藏
                    joke.setCollected(true);
                    GreenDaoHelper.getDaoSession().getJokeDao().insert(joke);
                }
                e.onNext(0);
                e.onComplete();
            }
        }, BackpressureStrategy.DROP);
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<Integer>() {
            @Override
            public void onNext(Integer integer) {
                //更新ui
                JokeSubContract.IJokeSubFragmentView view  = getView();
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
