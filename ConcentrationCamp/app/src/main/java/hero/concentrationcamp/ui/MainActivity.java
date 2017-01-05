package hero.concentrationcamp.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.umeng.socialize.UMShareAPI;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.MainContract;
import hero.concentrationcamp.mvp.presenter.MainActivityPresenter;
import hero.concentrationcamp.ui.common.base.BaseActivity;
import hero.concentrationcamp.ui.common.base.BaseFragment;
import hero.concentrationcamp.ui.collection.CollectioinFragment;
import hero.concentrationcamp.ui.gank.GankFragment;
import hero.concentrationcamp.ui.joke.JokeFragment;
import hero.concentrationcamp.utils.ToastUtils;
import io.reactivex.Flowable;

public class MainActivity extends BaseActivity<MainContract.IMainActivityView,MainActivityPresenter> implements MainContract.IMainActivityView{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private BaseFragment mCurrentFragment;
    private GankFragment mGankFragment;
    private JokeFragment mJokeFragment;
    private CollectioinFragment mCollectionFragment;
    private MenuItem mCurrentItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigation = (NavigationView)findViewById(R.id.navigation);
    }

    @Override
    public void initView() {
        initNavigation();
        initFragment();
    }

    private void switchFragment(BaseFragment to) {
        if (mCurrentFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (to.isAdded()) {
                transaction.hide(mCurrentFragment).show(to).commitAllowingStateLoss();
            } else {
                transaction.hide(mCurrentFragment).add(R.id.fl_main, to).commitAllowingStateLoss();
            }
        }
    }

    /**
     * 初始化侧边栏
     */
    private void initNavigation(){
        ColorStateList csl=getResources().getColorStateList(R.color.selector_menu_text);
        mNavigation.setItemTextColor(csl);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mCurrentItem.setChecked(false);
                mCurrentItem = item;
                switch (item.getItemId()) {
                    case R.id.drawer_gank:
                        switchFragment(mGankFragment);
                        mCurrentFragment = mGankFragment;
                        break;
                    case R.id.drawer_joke:
                        switchFragment(mJokeFragment);
                        mCurrentFragment = mJokeFragment;
                        break;
                    case R.id.drawer_collect:
                        switchFragment(mCollectionFragment);
                        mCurrentFragment = mCollectionFragment;
                        break;
                    case R.id.drawer_about:
//                        switchFragment(mGrilFragment);
//                        mCurrentFragment = mGrilFragment;
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 初始化fragment
     */
    private void initFragment(){
        mGankFragment = new GankFragment();
        mGankFragment.setDrawerLayout(mDrawerLayout);
        mJokeFragment = new JokeFragment();
        mJokeFragment.setDrawerLayout(mDrawerLayout);
        mCollectionFragment = new CollectioinFragment();
        mCollectionFragment.setDrawerLayout(mDrawerLayout);
        mCurrentFragment = mGankFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_main, mCurrentFragment)
                .commit();
        mCurrentItem = mNavigation.getMenu().findItem(R.id.drawer_gank);
        mCurrentItem.setChecked(true);
    }

    @Override
    protected MainActivityPresenter createPresenter() {
        return new MainActivityPresenter();
    }

    @Override
    protected int getCreateViewLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mNavigation.isShown()) {
            mDrawerLayout.closeDrawers();
        } else {
            showExit();
        }
    }
    // 连续两次back，切换应用到后台
    private static long lastBackTime;
    /**
     * 退出
     */
    private void showExit() {
        if (lastBackTime + 2000 > System.currentTimeMillis()) {
            // 返回到后台运行
            moveTaskToBack(true);
        } else {
            ToastUtils.showToast("再按一次退出程序");
        }
        lastBackTime = System.currentTimeMillis();
    }
}
