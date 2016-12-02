package hero.concentrationcamp.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.contract.MainContract;
import hero.concentrationcamp.mvp.presenter.MainActivityPresenter;
import hero.concentrationcamp.ui.gank.GankFragment;
import hero.concentrationcamp.utils.ToastUtils;

public class MainActivity extends BaseActivity<MainContract.IMainActivityView,MainActivityPresenter> implements MainContract.IMainActivityView{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;
    private MenuItem mCurrentItem;
    private BaseFragment mCurrentFragment;
    private GankFragment mGankFragment;
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
    private void initNavigation(){
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ColorStateList csl=getResources().getColorStateList(R.color.selector_menu_text);
        mNavigation.setItemTextColor(csl);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mCurrentItem.setChecked(false);
                mCurrentItem = item;
                switch (item.getItemId()) {
                    case R.id.drawer_gank:
//                        switchFragment(mTodayFragment);
//                        mCurrentFragment = mTodayFragment;
                        break;
                    case R.id.drawer_duanzi:
//                        switchFragment(mLikeFragment);
//                        mCurrentFragment = mLikeFragment;
                        break;
                    case R.id.drawer_meipai:
//                        switchFragment(mAboutFragment);
//                        mCurrentFragment = mAboutFragment;
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
//        mTodayFragment = new TodayInHistoryFragment();
//        mLikeFragment = new LikeFragment();
//        mAboutFragment = new AboutFragment();
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
