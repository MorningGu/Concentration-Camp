package hero.concentrationcamp.ui.joke;

import hero.concentrationcamp.mvp.contract.JokeContract;
import hero.concentrationcamp.mvp.presenter.JokeFragmentPresenter;
import hero.concentrationcamp.ui.BaseFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class JokeFragment extends BaseFragment<JokeContract.IJokeFragmentView,JokeFragmentPresenter>implements JokeContract.IJokeFragmentView{
    @Override
    protected JokeFragmentPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getCreateViewLayoutId() {
        return 0;
    }
}
