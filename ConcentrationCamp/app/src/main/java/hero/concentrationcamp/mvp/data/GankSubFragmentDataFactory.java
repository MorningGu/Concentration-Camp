package hero.concentrationcamp.mvp.data;

import java.util.ArrayList;
import java.util.List;

import hero.concentrationcamp.mvp.model.entity.GankVo;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.retrofit.ApiManager;
import io.reactivex.Flowable;

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
    public Flowable<GankVo> getDataList(SourceColumn column, int pageNo){
        return ApiManager.INSTANCE.getICampInterface().getGankList(column.getCode(),10,pageNo);
    }
}
