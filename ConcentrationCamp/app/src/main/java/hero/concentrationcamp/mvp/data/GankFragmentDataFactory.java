package hero.concentrationcamp.mvp.data;

import java.util.ArrayList;
import java.util.List;

import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.ui.BaseFragment;
import hero.concentrationcamp.ui.gank.GankSubFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankFragmentDataFactory {
    private SourceColumn[] gankColumns = {new SourceColumn("Android","Android"),new SourceColumn("iOS","iOS"),new SourceColumn("休息视频","休息视频")
            ,new SourceColumn("福利","福利"),new SourceColumn("拓展资源","拓展资源"),new SourceColumn("前端","前端")
            ,new SourceColumn("瞎推荐","瞎推荐"),new SourceColumn("App","App")};
    private SourceColumn[] duanziColumns = {new SourceColumn("段子","QueryJokeByTime"),new SourceColumn("趣图","QueryImgByTime")};

    private List<BaseFragment> fragments;

    public SourceColumn[] getGankColumns() {
        return gankColumns;
    }

    public SourceColumn[] getDuanziColumns() {
        return duanziColumns;
    }

    /**
     * 获得所有条目的fragment
     * @return
     */
    public List<BaseFragment> getFragments() {
        if(fragments==null){
            fragments = new ArrayList<>();
            for(SourceColumn column:gankColumns){
                GankSubFragment fragment = new GankSubFragment();
                fragment.setColumn(column);
                fragments.add(fragment);
            }
        }
        return fragments;
    }
}
