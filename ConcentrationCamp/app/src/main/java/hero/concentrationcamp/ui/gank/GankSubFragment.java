package hero.concentrationcamp.ui.gank;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.GankSubContract;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.GankSubFragmentPresenter;
import hero.concentrationcamp.ui.BaseFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankSubFragment extends BaseFragment<GankSubContract.IGankSubFragmentView,GankSubFragmentPresenter> implements GankSubContract.IGankSubFragmentView {
    private SourceColumn mColumn;

    @Override
    protected GankSubFragmentPresenter createPresenter() {
        return new GankSubFragmentPresenter();
    }

    @Override
    protected int getCreateViewLayoutId() {
        return R.layout.fragment_gank_sub;
    }
    public void setColumn(SourceColumn column){
        this.mColumn = column;
    }
}
