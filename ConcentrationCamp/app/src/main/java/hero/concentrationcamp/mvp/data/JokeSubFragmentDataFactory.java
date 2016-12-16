package hero.concentrationcamp.mvp.data;


import java.util.List;

import hero.concentrationcamp.GConfig;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.JokeDao;
import hero.concentrationcamp.mvp.model.entity.JokeVo;
import hero.concentrationcamp.mvp.model.entity.YiYuanResult;
import hero.concentrationcamp.mvp.model.greendao.GreenDaoHelper;
import hero.concentrationcamp.retrofit.ApiManager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeSubFragmentDataFactory {
    /**
     *
     * @param type
     * @param pageNo
     * @return
     */
    public ResourceSubscriber getDataList(ResourceSubscriber subscriber, String type, int pageNo){
        Flowable<YiYuanResult<JokeVo<Joke>>> flowable = ApiManager.INSTANCE.getIJokeInterface()
                .postQueryJoke(type, GConfig.YIYUAN_SIGN, GConfig.YIYUAN_APP_ID,pageNo,10);
        return flowable.subscribeOn(Schedulers.io())
                .filter(new Predicate<YiYuanResult<JokeVo<Joke>>>() {
                    @Override
                    public boolean test(YiYuanResult<JokeVo<Joke>> jokeVoYiYuanResult) throws Exception {
                        if(jokeVoYiYuanResult==null || jokeVoYiYuanResult.getShowapi_res_body()==null
                                ||jokeVoYiYuanResult.getShowapi_res_body().getContentlist()==null){
                            return true;
                        }
                        for(Joke joke:jokeVoYiYuanResult.getShowapi_res_body().getContentlist()){
                            //对每条数据做本地是否收藏的查询，并赋值
                            Joke temp =  GreenDaoHelper.getDaoSession().getJokeDao().queryBuilder()
                                    .where(JokeDao.Properties.S_id.eq(joke.getS_id())).limit(1).unique();
                            if(temp!=null){
                                joke.setCollected(true);
                            }
                        }
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }
    /**
     * 获取本地收藏的gank
     * @param subscriber
     * @param tid 主键id
     * @param count 每页的数量
     * @return
     */
    public ResourceSubscriber getDataList(ResourceSubscriber subscriber,final boolean isRefresh,final Long tid,final int count){
        Flowable<List<Joke>> flowable = Flowable.create(new FlowableOnSubscribe<List<Joke>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Joke>> e) throws Exception {
                List<Joke> jokes ;
                if(isRefresh){
                    jokes = GreenDaoHelper.getDaoSession().getJokeDao()
                            .queryBuilder().orderDesc(JokeDao.Properties.T_id)
                            .limit(count).list();
                }else{
                    jokes = GreenDaoHelper.getDaoSession().getJokeDao()
                            .queryBuilder().where(JokeDao.Properties.T_id.lt(tid))
                            .orderDesc(JokeDao.Properties.T_id).limit(count).list();
                }
                e.onNext(jokes);
                e.onComplete();
            }
        }, BackpressureStrategy.DROP);
        //flowable 链式结构最好不要打断，打断后单独运行filter map等都不执行里面的方法
        return flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }
}
