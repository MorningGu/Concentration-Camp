package hero.concentrationcamp.retrofit;

import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.JokeB;
import hero.concentrationcamp.mvp.model.entity.JokeVo;
import hero.concentrationcamp.mvp.model.entity.YiYuanResult;
import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 段子的网络请求
 * Created by hero on 2016/12/6 0006.
 */

public interface IJokeInterface {
    /**
     * 易源自营段子
     * @param type 类型 1为文字，2为jpg，3为动图
     * @param sign 签名
     * @param appid appid
     * @param pageNo 请求页码数
     * @param dataSize 一页数据数量
     * @return
     */
    @FormUrlEncoded
    @POST("341-{type}")
    Flowable<YiYuanResult<JokeVo<Joke>>> postQueryJoke(@Path("type") String type, @Field("showapi_sign") String sign, @Field("showapi_appid") int appid, @Field("page") int pageNo, @Field("maxResult") int dataSize);
    /**
     * 百思不得姐段子
     * @param type 类型 查询的类型，默认全部返回。type=10 图片type=29 段子type=31 声音type=41 视频
     * @param sign 签名
     * @param appid appid
     * @param pageNo 请求页码数 一页默认20条
     * @param text 文本中的数据，用于模糊查询
     * @return
     */
    @FormUrlEncoded
    @POST("341-1")
    Flowable<YiYuanResult<JokeVo<JokeB>>> postQueryJokeB(@Field("type") String type, @Field("showapi_sign") String sign, @Field("showapi_appid") int appid, @Field("page") int pageNo, @Field("title") String text);
}
