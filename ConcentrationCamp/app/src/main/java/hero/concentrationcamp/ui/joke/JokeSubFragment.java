package hero.concentrationcamp.ui.joke;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.JokeSubContract;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.JokeSubFragmentPresenter;
import hero.concentrationcamp.ui.base.BaseActivity;
import hero.concentrationcamp.ui.base.BaseFragment;
import hero.concentrationcamp.ui.ImageActivity;
import hero.concentrationcamp.ui.adapter.JokeDataAdapter;
import hero.concentrationcamp.utils.UmengShare;

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
        mAdapter = new JokeDataAdapter(null, ((BaseActivity)getActivity()).mScreenWidth);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
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
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_image:{
                        Intent intent = new Intent(getActivity(), ImageActivity.class);
                        intent.putExtra("url",((Joke)(adapter.getData().get(position))).getImg());
                        if(((Joke)(adapter.getData().get(position))).getType()==1){
                            //文字的时候显示text
                            intent.putExtra("text",((Joke)(adapter.getData().get(position))).getText());
                        }else{
                            //图片的时候显示title
                            intent.putExtra("text",((Joke)(adapter.getData().get(position))).getTitle());
                        }
                        startActivity(intent);
                        break;
                    }
                    case R.id.btn_collect:
                        mPresenter.setCollectState(position,(Joke)(adapter.getData().get(position)));
                        break;
                    case R.id.btn_share:{
                        String text;
                        if(((Joke)(adapter.getData().get(position))).getType()==1){
                            //文字的时候显示text
                            text = ((Joke)(adapter.getData().get(position))).getText();
                        }else{
                            //图片的时候显示title
                            text = ((Joke)(adapter.getData().get(position))).getTitle();
                        }
                        if(TextUtils.isEmpty(((Joke)(adapter.getData().get(position))).getImg())){
                            new UmengShare().openShareBoard(getActivity(),text,text);
                        }else{
                            UMImage image = new UMImage(getActivity(), ((Joke)(adapter.getData().get(position))).getImg());//网络图片
                            new UmengShare().openShareBoard(getActivity(),text,text,((Joke)(adapter.getData().get(position))).getImg(),image);
                        }
                        break;
                    }
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
