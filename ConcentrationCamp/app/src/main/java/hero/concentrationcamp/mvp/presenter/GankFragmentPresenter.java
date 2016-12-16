package hero.concentrationcamp.mvp.presenter;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.contract.GankContract;
import hero.concentrationcamp.mvp.data.FragmentDataFactory;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankFragmentPresenter extends BasePresenter<GankContract.IGankFragmentView> implements GankContract.IGankFragmetnPresenter {
    FragmentDataFactory factory = new FragmentDataFactory();
    @Override
    public void startGank() {
        GankContract.IGankFragmentView view = getView();
        view.initPager(factory.getGankSubFragments(),factory.getGankColumns());
    }

    @Override
    public void startJoke() {
        GankContract.IGankFragmentView view = getView();
        view.initPager(factory.getJokeSubFragments(),factory.getJokeColumns());
    }

    @Override
    public void startCollection() {
        GankContract.IGankFragmentView view = getView();
        view.initPager(factory.getCollectionSubFragments(),factory.getCollectionColumns());
    }
}
