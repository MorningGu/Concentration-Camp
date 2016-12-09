package hero.concentrationcamp.mvp.data;

import java.util.ArrayList;
import java.util.List;

import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.ui.BaseFragment;
import hero.concentrationcamp.ui.gank.GankFragment;
import hero.concentrationcamp.ui.gank.GankSubFragment;
import hero.concentrationcamp.ui.joke.JokeSubFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class FragmentDataFactory {
    private SourceColumn[] gankColumns = {new SourceColumn("Android","Android"),new SourceColumn("iOS","iOS"),new SourceColumn("休息视频","休息视频")
            ,new SourceColumn("福利","福利"),new SourceColumn("拓展资源","拓展资源"),new SourceColumn("前端","前端")
            ,new SourceColumn("瞎推荐","瞎推荐"),new SourceColumn("App","App")};
    private SourceColumn[] jokeColumns = {new SourceColumn("段子","1"),new SourceColumn("趣图","2"),new SourceColumn("GIF","3")};
    public SourceColumn[] getGankColumns(){
        return gankColumns;
    }
    public SourceColumn[] getJokeColumns(){
        return jokeColumns;
    }
    /**
     * 获得所有条目的GankSubFragment
     * @return
     */
    public List<BaseFragment> getGankSubFragments() {

        List<BaseFragment> gankSubFragments = new ArrayList<>();
            for(SourceColumn column:gankColumns){
                GankSubFragment fragment = new GankSubFragment();
                fragment.setColumn(column);
                gankSubFragments.add(fragment);
            }
        return gankSubFragments;
    }
    /**
     * 获得所有条目的JokeSubFragment
     * @return
     */
    public List<BaseFragment> getJokeSubFragments() {
        List<BaseFragment> jokeSubFragments = new ArrayList<>();
        for(SourceColumn column:jokeColumns){
            JokeSubFragment fragment = new JokeSubFragment();
            fragment.setColumn(column);
            jokeSubFragments.add(fragment);
        }
        return jokeSubFragments;
    }
}
