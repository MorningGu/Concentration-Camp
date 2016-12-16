package hero.concentrationcamp.mvp.contract;

import java.util.List;

import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;

/**
 * Created by hero on 2016/12/8 0008.
 */

public interface JokeSubContract {
    interface IJokeSubFragmentView{
        void updateData(boolean isRefresh, List<Joke> data);
        void updateItemState(int position);
    }
    interface IJokeSubFragmentPresenter{
        void getJokeData(String type,int pageNo,boolean isRefresh);
        void setCollectState(int position,Joke joke);
    }
}
