package hero.concentrationcamp.mvp.data;

import hero.concentrationcamp.Config;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.JokeVo;
import hero.concentrationcamp.mvp.model.entity.YiYuanResult;
import hero.concentrationcamp.retrofit.ApiManager;
import io.reactivex.Flowable;

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
    public Flowable<YiYuanResult<JokeVo<Joke>>> getDataList(String type, int pageNo){
        return ApiManager.INSTANCE.getIJokeInterface().postQueryJoke(type,Config.YIYUAN_SIGN,Config.YIYUAN_APP_ID,pageNo,10);
    }
}
