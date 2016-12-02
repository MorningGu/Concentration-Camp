package hero.concentrationcamp.mvp.contract;

import java.util.List;

import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.ui.BaseFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public interface GankContract {
    interface IGankFragmentView{
        /**
         * 初始化viewpager
         * @param fragments
         * @param columns
         */
        void initPager(List<BaseFragment> fragments, SourceColumn[] columns);
    }
    interface IGankFragmetnPresenter{
        /**
         * 初始化一些
         */
        void start();
    }
}