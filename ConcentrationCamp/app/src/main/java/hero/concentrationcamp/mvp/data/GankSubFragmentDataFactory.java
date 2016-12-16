package hero.concentrationcamp.mvp.data;

import java.util.List;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by hero on 2016/12/6 0006.
 */

public class GankSubFragmentDataFactory {
    /**
     * 获取数据
     * @param column
     * @param pageNo
     * @return
     */
    public ResourceSubscriber getDataList(ResourceSubscriber subscriber,SourceColumn column, int pageNo){
        Flowable<GankVo> flowable = ApiManager.INSTANCE.getIGankInterface().getGankList(column.getCode(),10,pageNo);
        //flowable 链式结构最好不要打断，打断后单独运行filter map等都不执行里面的方法
        return flowable.subscribeOn(Schedulers.io())
                .filter(new Predicate<GankVo>() {
                    @Override
                    public boolean test(GankVo gankVo) throws Exception {
                        if(gankVo==null || gankVo.getResults()==null){
                            return true;
                        }
                        for(Gank gank:gankVo.getResults()){
                            //对每条数据做本地是否收藏的查询，并赋值
                            Gank temp = GreenDaoHelper.getDaoSession().getGankDao().queryBuilder()
                                    .where(GankDao.Properties.S_id.eq(gank.getS_id())).limit(1).unique();
                            if(temp!=null){
                                gank.setCollected(true);
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
        Flowable<List<Gank>> flowable = Flowable.create(new FlowableOnSubscribe<List<Gank>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Gank>> e) throws Exception {
                List<Gank> ganks ;
                if(isRefresh){
                    ganks = GreenDaoHelper.getDaoSession().getGankDao()
                            .queryBuilder().orderDesc(GankDao.Properties.T_id)
                            .limit(count).list();
                }else{
                    ganks = GreenDaoHelper.getDaoSession().getGankDao()
                            .queryBuilder().where(GankDao.Properties.T_id.lt(tid))
                            .orderDesc(GankDao.Properties.T_id).limit(count).list();
                }
                e.onNext(ganks);
                e.onComplete();
            }
        }, BackpressureStrategy.DROP);
        //flowable 链式结构最好不要打断，打断后单独运行filter map等都不执行里面的方法
        return flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }
}
