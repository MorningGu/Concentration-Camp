package hero.concentrationcamp.mvp.presenter;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.contract.JokeContract;
import hero.concentrationcamp.mvp.data.FragmentDataFactory;
import hero.concentrationcamp.ui.joke.JokeFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class JokeFragmentPresenter extends BasePresenter<JokeFragment> implements JokeContract.IJokeFragmentPresenter {
    FragmentDataFactory factory = new FragmentDataFactory();
    @Override
    public void start() {
        JokeFragment view = getView();
        view.initPager(factory.getJokeSubFragments(),factory.getJokeColumns());
    }
}
