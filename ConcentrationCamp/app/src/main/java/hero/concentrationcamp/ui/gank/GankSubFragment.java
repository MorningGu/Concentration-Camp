package hero.concentrationcamp.ui.gank;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.GankSubContract;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.GankSubFragmentPresenter;
import hero.concentrationcamp.ui.common.adapter.MultiGankAdapter;
import hero.concentrationcamp.ui.common.base.BaseActivity;
import hero.concentrationcamp.ui.common.base.BaseFragment;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class GankSubFragment extends BaseFragment<GankSubContract.IGankSubFragmentView,GankSubFragmentPresenter> implements GankSubContract.IGankSubFragmentView {
    private SourceColumn mColumn;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MultiGankAdapter mAdapter;
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
                mAdapter.openLoadMore(10);
                mPresenter.getGankData(mColumn,1,true);
            }
        });
        //设置刷新时动画的颜色，可以设置4个
        // 顶部刷新的样式
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mAdapter = new MultiGankAdapter(((BaseActivity)getActivity()).mScreenWidth,null,mPresenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(null);//解决更新单个item时的闪烁
        mRecyclerView.setAdapter(mAdapter);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_empty,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mAdapter.setEmptyView(view);
        mAdapter.openLoadMore(10);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener(){
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getGankData(mColumn,pageNo+1,false);
            }
        });
        mPresenter.bindRxBus();
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
        mAdapter.loadComplete();
    }

    @Override
    public void updateData(boolean isRefresh, List<Gank> data) {
        if(data==null || data.size()==0){
            showError("没有数据了");
            return;
        }
        //如果小于10，不需要再执行loadMore了
        if(data.size()<10){
            mAdapter.loadComplete();
        }
        if(isRefresh){
            mSwipeRefreshLayout.setRefreshing(false);
            pageNo=1;
            mAdapter.setNewData(data);
        }else{
            pageNo++;
            mAdapter.addData(data);
        }
    }

    @Override
    public void updateItemState(int position) {
        //单个更新有闪烁的问题
//        mAdapter.notifyDataSetChanged();
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void updateData(Gank gank) {
        if(mAdapter.getDetail()==null){
            return;
        }
        //收藏状态在其他页面变更后，在这里要进行更新
        mAdapter.getDetail().setCollected(gank.isCollected());
        mAdapter.notifyDataSetChanged();
    }

}
