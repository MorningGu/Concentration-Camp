package hero.concentrationcamp.retrofit;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import hero.concentrationcamp.GApplication;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.GankDao;
import hero.concentrationcamp.mvp.model.entity.GankVo;
import hero.concentrationcamp.mvp.model.greendao.GreenDaoHelper;
import hero.concentrationcamp.utils.LogUtils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hero on 2016/9/6 0006.
 */

public enum ApiManager {
    INSTANCE;
    private IGankInterface gankInterface;
    private IJokeInterface jokeInterface;
    private Object monitor = new Object();
    /**
     * 这一部分配置常量，可以抽取出常量类
     */
    private static final long DEFAULT_TIMEOUT = 20000;//默认超时时间(毫秒)

    /**
     * 取得实例化的Retrofit
     * 可以根据不同的需求获取不同的Retrofit实例
     * @return
     */
    public IGankInterface getIGankInterface(){
        if (gankInterface == null) {
            synchronized (monitor) {
                if (gankInterface == null) {
                    //打印拦截器
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(GApplication.getInstance().isDebug()?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
                    // 公私密匙
                    //MarvelSigningInterceptor signingInterceptor = new MarvelSigningInterceptor(KeyValue.MARVEL_PUBLIC_KEY, KeyValue.MARVEL_PRIVATE_KEY);
                    OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
                    okHttpClient.addNetworkInterceptor(new HTTPInterceptor())
                            .retryOnConnectionFailure(true)//设置出现错误进行重新连接。;
                            //.addInterceptor(signingInterceptor)//加密处理
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .addInterceptor(logging);

                    gankInterface = new Retrofit.Builder()
                            .client(okHttpClient.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl("http://gank.io/api/")
                            .build().create(IGankInterface.class);
                }
            }
        }
        return gankInterface;
    }
    /**
     * 取得实例化的Retrofit
     * 可以根据不同的需求获取不同的Retrofit实例
     * @return
     */
    public IJokeInterface getIJokeInterface(){
        if (jokeInterface == null) {
            synchronized (monitor) {
                if (jokeInterface == null) {
                    //打印拦截器
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(GApplication.getInstance().isDebug()?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
                    // 公私密匙
                    //MarvelSigningInterceptor signingInterceptor = new MarvelSigningInterceptor(KeyValue.MARVEL_PUBLIC_KEY, KeyValue.MARVEL_PRIVATE_KEY);
                    OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
                    okHttpClient.addNetworkInterceptor(new HTTPInterceptor())
                            .retryOnConnectionFailure(true)//设置出现错误进行重新连接。;
                            //.addInterceptor(signingInterceptor)//加密处理
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .addInterceptor(logging);

                    jokeInterface = new Retrofit.Builder()
                            .client(okHttpClient.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl("http://route.showapi.com/")
                            .build().create(IJokeInterface.class);
                }
            }
        }
        return jokeInterface;
    }
    /**
     * 初始化通用的观察者
     * @param observable 观察者
     */
    public ResourceSubscriber startObservable(Flowable observable, ResourceSubscriber subscriber) {
       return (ResourceSubscriber)observable.subscribeOn(Schedulers.io())
               .filter(new Predicate() {
                   @Override
                   public boolean test(Object o) throws Exception {
                        if(o instanceof GankVo){
                            for(Gank gank:((GankVo)o).getResults()){
                                //对每条数据做本地是否收藏的查询，并赋值
                                Gank temp = GreenDaoHelper.getDaoSession().getGankDao().queryBuilder()
                                        .where(GankDao.Properties.S_id.eq(gank.getS_id())).unique();
                                if(temp!=null){
                                    gank.setCollected(temp.isCollected());
                                }
                            }
                        }
                       return true;
                   }
               })
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(subscriber);
    }
}
