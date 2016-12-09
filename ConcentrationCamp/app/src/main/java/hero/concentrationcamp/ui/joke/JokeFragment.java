package hero.concentrationcamp.ui.joke;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.JokeContract;
import hero.concentrationcamp.mvp.model.entity.SourceColumn;
import hero.concentrationcamp.mvp.presenter.JokeFragmentPresenter;
import hero.concentrationcamp.ui.BaseFragment;
import hero.concentrationcamp.ui.adapter.SubFragmentAdapter;

/**
 * Created by hero on 2016/12/2 0002.
 */

public class JokeFragment extends BaseFragment<JokeFragment,JokeFragmentPresenter>implements JokeContract.IJokeFragmentView{
    private TabLayout tabLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager viewPager;
    private SubFragmentAdapter adapter;
    private DrawerLayout mDrawerLayout;
    @Override
    public void findView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initToolBar();
        initDrawerToggle();
        mPresenter.start();
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar(){
        mToolbar.setTitle("段子");//设置Toolbar标题
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    /**
     * 初始化toolbar中的返回键
     */
    private void initDrawerToggle(){
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }
    @Override
    public void initPager(List<BaseFragment> fragments, SourceColumn[] columns) {
        viewPager.setOffscreenPageLimit(2);
        adapter = new SubFragmentAdapter(getChildFragmentManager(), fragments,columns);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 设置mDrawerLayout 在new Fragment的时候设置
     * @param drawerLayout
     */
    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
    }
    @Override
    protected JokeFragmentPresenter createPresenter() {
        return new JokeFragmentPresenter();
    }

    @Override
    protected int getCreateViewLayoutId() {
        return R.layout.fragment_joke;
    }
}
