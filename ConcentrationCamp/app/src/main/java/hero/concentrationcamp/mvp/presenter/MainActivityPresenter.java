package hero.concentrationcamp.mvp.presenter;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.contract.MainContract;
import hero.concentrationcamp.mvp.data.MainActivityDataFactory;

/**
 * Created by hero on 2016/11/30 0030.
 */

public class MainActivityPresenter extends BasePresenter<MainContract.IMainActivityView> implements MainContract.IMainPresenter {
    MainActivityDataFactory factory = new MainActivityDataFactory();

}
