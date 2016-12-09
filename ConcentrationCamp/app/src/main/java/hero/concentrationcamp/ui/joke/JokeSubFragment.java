package hero.concentrationcamp.ui.joke;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.JokeSubContract;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.JokeSubFragmentPresenter;
import hero.concentrationcamp.ui.BaseFragment;
import hero.concentrationcamp.ui.ImageActivity;
import hero.concentrationcamp.ui.adapter.JokeDataAdapter;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeSubFragment extends BaseFragment<JokeSubFragment,JokeSubFragmentPresenter> implements JokeSubContract.IJokeSubFragmentView {
    private SourceColumn mColumn;
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
                mPresenter.getJokeData(mColumn.getCode(),1,true);
            }
        });
        //设置刷新时动画的颜色，可以设置4个
        // 顶部刷新的样式
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mAdapter = new JokeDataAdapter(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.openLoadMore(10);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener(){
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getJokeData(mColumn.getCode(),pageNo+1,false);
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.iv_image:{
                        Intent intent = new Intent(getActivity(), ImageActivity.class);
                        intent.putExtra("url",((Joke)(adapter.getData().get(position))).getImg());
                        startActivity(intent);
                        break;
                    }
                    case R.id.btn_collect:
                        break;
                    case R.id.btn_share:
                        break;
                    default:
                        break;
                }
            }
        });
        mPresenter.getJokeData(mColumn.getCode(),1,true);
    }
    @Override
    public void showError(String msg) {
        super.showError(msg);
        //// TODO: 2016/12/6 0006  状态的变更
        mSwipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void updateData(boolean isRefresh, List<Joke> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(isRefresh){
            pageNo=1;
            mAdapter.setNewData(data);
        }else{
            pageNo++;
            mAdapter.addData(data);
        }
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
