package hero.concentrationcamp.mvp.presenter;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.contract.GankContract;
import hero.concentrationcamp.mvp.data.GankFragmentDataFactory;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankFragmentPresenter extends BasePresenter<GankContract.IGankFragmentView> implements GankContract.IGankFragmetnPresenter {
    GankFragmentDataFactory factory = new GankFragmentDataFactory();
    @Override
    public void start() {
        GankContract.IGankFragmentView view = getView();
        view.initPager(factory.getFragments(),factory.getGankColumns());
    }
}
