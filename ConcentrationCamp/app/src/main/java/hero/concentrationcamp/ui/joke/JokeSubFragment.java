package hero.concentrationcamp.ui.joke;

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
import hero.concentrationcamp.mvp.contract.JokeSubContract;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.JokeSubFragmentPresenter;
import hero.concentrationcamp.ui.common.base.BaseActivity;
import hero.concentrationcamp.ui.common.base.BaseFragment;
import hero.concentrationcamp.ui.common.adapter.JokeDataAdapter;

/**
 * 易源数据提供的笑话的实际展示的每种类型的子页面
 * Created by hero on 2016/12/8 0008.
 */

public class JokeSubFragment extends BaseFragment<JokeSubContract.IJokeSubFragmentView,JokeSubFragmentPresenter> implements JokeSubContract.IJokeSubFragmentView {

    private SourceColumn mColumn;//具体的栏目
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private JokeDataAdapter mAdapter;
    private int pageNo = 1;

    @Override
    public void findView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_joke);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_joke);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.openLoadMore(10);
                mPresenter.getJokeData(mColumn.getCode(),1,true);
            }
        });
        //设置刷新时动画的颜色，可以设置4个
        // 顶部刷新的样式
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mAdapter = new JokeDataAdapter(null, ((BaseActivity)getActivity()).mScreenWidth,mPresenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(null);//解决更新单个item时的闪烁
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.openLoadMore(10);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_empty,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mAdapter.setEmptyView(view);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener(){
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getJokeData(mColumn.getCode(),pageNo+1,false);
            }
        });
        mPresenter.getJokeData(mColumn.getCode(),1,true);
    }
    @Override
    public void showError(String msg) {
        super.showError(msg);
        //// TODO: 2016/12/6 0006  状态的变更
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.loadComplete();
    }
    @Override
    public void updateData(boolean isRefresh, List<Joke> data) {
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
//        mAdapter.notifyDataSetChanged();
        //为了解决更新时图片闪烁的问题
        mAdapter.notifyItemChanged(position);
    }

    @Override
    protected JokeSubFragmentPresenter createPresenter() {
        return new JokeSubFragmentPresenter();
    }

    @Override
    protected int getCreateViewLayoutId() {
        return R.layout.fragment_joke_sub;
    }

    public void setColumn(SourceColumn column){
        mColumn = column;
    }
}
