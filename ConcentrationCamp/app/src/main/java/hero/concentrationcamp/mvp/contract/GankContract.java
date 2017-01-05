package hero.concentrationcamp.mvp.contract;

import android.support.v4.widget.DrawerLayout;

import java.util.List;

import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.ui.common.base.BaseFragment;

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
        void initToolBar();
        void initDrawerToggle();
        void setDrawerLayout(DrawerLayout drawerLayout);
    }
    interface IGankFragmetnPresenter{
        /**
         * 初始化一些
         */
        void startGank();
        void startJoke();
        void startCollection();
    }
}
