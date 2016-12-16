package hero.concentrationcamp.ui.gank;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.GankSubContract;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.GankSubFragmentPresenter;
import hero.concentrationcamp.ui.adapter.MultiGankAdapter;
import hero.concentrationcamp.ui.base.BaseActivity;
import hero.concentrationcamp.ui.base.BaseFragment;
import hero.concentrationcamp.ui.adapter.GankDataAdapter;
import hero.concentrationcamp.utils.UmengShare;

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
        mAdapter = new MultiGankAdapter(((BaseActivity)getActivity()).mScreenWidth,null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
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
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {}

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.btn_share:{
                        if("福利".equals(((Gank)adapter.getData().get(position)).getType())){
                            UMImage image = new UMImage(getActivity(), ((Gank)adapter.getData().get(position)).getUrl());//网络图片
                            new UmengShare().openShareBoard(getActivity(),null
                                    , ((Gank)adapter.getData().get(position)).getDesc()
                                    , ((Gank)adapter.getData().get(position)).getUrl()
                                    ,image);
                        }else{
                            if(((Gank)adapter.getData().get(position)).getImages()!=null
                                    && ((Gank)adapter.getData().get(position)).getImages().size()>0){
                                UMImage image = new UMImage(getActivity(), ((Gank)adapter.getData().get(position)).getImages().get(0));//网络图片
                                new UmengShare().openShareBoard(getActivity(),null
                                        , ((Gank)adapter.getData().get(position)).getDesc()
                                        , ((Gank)adapter.getData().get(position)).getUrl()
                                        ,image);
                            }else{
                                new UmengShare().openShareBoard(getActivity(),null
                                        , ((Gank)adapter.getData().get(position)).getDesc()
                                        , ((Gank)adapter.getData().get(position)).getUrl(),null);
                            }

                        }
                        break;
                    }
                    case R.id.btn_collect:{
                        mPresenter.setCollectState(position,((Gank)adapter.getData().get(position)));
                    }
                }
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
        mAdapter.notifyItemChanged(position);
    }
}
