package hero.concentrationcamp.mvp.presenter;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.contract.GankContract;
import hero.concentrationcamp.mvp.data.FragmentDataFactory;
import hero.concentrationcamp.ui.gank.GankFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankFragmentPresenter extends BasePresenter<GankFragment> implements GankContract.IGankFragmetnPresenter {
    FragmentDataFactory factory = new FragmentDataFactory();
    @Override
    public void start() {
        GankFragment view = getView();
        view.initPager(factory.getGankSubFragments(),factory.getGankColumns());
    }
}
