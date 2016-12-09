package hero.concentrationcamp.mvp.presenter;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.contract.JokeSubContract;
import hero.concentrationcamp.mvp.data.JokeSubFragmentDataFactory;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.JokeVo;
import hero.concentrationcamp.mvp.model.entity.YiYuanResult;
import hero.concentrationcamp.retrofit.ApiManager;
import hero.concentrationcamp.ui.joke.JokeSubFragment;
import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeSubFragmentPresenter extends BasePresenter<JokeSubFragment> implements JokeSubContract.IJokeSubFragmentPresenter {
    JokeSubFragmentDataFactory factory = new JokeSubFragmentDataFactory();

    @Override
    public void getJokeData(String type, int pageNo, final boolean isRefresh) {
        Flowable<YiYuanResult<JokeVo<Joke>>> flowable = factory.getDataList(type,pageNo);
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<YiYuanResult>() {
            @Override
            public void onNext(YiYuanResult vo) {
                JokeSubFragment view  = getView();
                if(vo.getShowapi_res_code()!=0){
                    //// TODO: 2016/12/6 0006 提示服务器错误
                    view.showError("服务器错误");
                    return;
                }
                if(vo.getShowapi_res_body()==null){
                    //// TODO: 2016/12/6 0006  提示没数据
                    view.showError("没有数据了");
                    return;
                }
                view.updateData(isRefresh,vo.getShowapi_res_body().getContentlist());
            }

            @Override
            public void onError(Throwable t) {
                //// TODO: 2016/12/6 0006 提示网络错误
                JokeSubFragment view  = getView();
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
