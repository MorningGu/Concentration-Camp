package hero.concentrationcamp.ui.gank;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.GankSubContract;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.GankSubFragmentPresenter;
import hero.concentrationcamp.ui.BaseFragment;
import hero.concentrationcamp.ui.adapter.GankDataAdapter;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankSubFragment extends BaseFragment<GankSubFragment,GankSubFragmentPresenter> implements GankSubContract.IGankSubFragmentView {
    private SourceColumn mColumn;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GankDataAdapter mAdapter;
    private int pageNo = 1;
    @Override
    protected GankSubFragmentPresenter createPresenter() {
        return new GankSubFragmentPresenter();
    }

    @Override
    public void findView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_gank);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_gank);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getGankData(mColumn,1,true);
            }
        });
        //设置刷新时动画的颜色，可以设置4个
        // 顶部刷新的样式
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mAdapter = new GankDataAdapter(mColumn,null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.openLoadMore(10);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener(){
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getGankData(mColumn,pageNo+1,false);
            }
        });
        mPresenter.getGankData(mColumn,1,true);
    }

    @Override
    protected int getCreateViewLayoutId() {
        return R.layout.fragment_gank_sub;
    }
    public void setColumn(SourceColumn column){
        this.mColumn = column;
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        //// TODO: 2016/12/6 0006  状态的变更
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateData(boolean isRefresh, List<Gank> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(isRefresh){
            pageNo=1;
        }else{
            pageNo++;
        }
        mAdapter.addData(data);
    }
}
