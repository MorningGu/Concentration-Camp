package hero.concentrationcamp.ui.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.IBaseView;
import hero.concentrationcamp.utils.AppManager;
import hero.concentrationcamp.utils.LogUtils;
import hero.concentrationcamp.utils.ToastUtils;


/**
 * Created by hero on 2016/11/3 0003.
 */

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity implements IBaseView{
    public int mScreenWidth;
    public int mScreenHeight;
    public int mStatusBarHeight;
    protected T mPresenter;
    private WeakReference<Activity> mWeakReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeakReference = new WeakReference<Activity>(this);
        AppManager.INSTANCE.addActivity(mWeakReference);
        setContentView(getCreateViewLayoutId());
        findView();
        initView();
        initPresenter();
        initScreenData();
    }

    /**
     * 用于BaseActivity初始化Presenter
     */
    public void initPresenter(){
        //初始化Presenter
        mPresenter = createPresenter();
        //presenter与View绑定
        if(null != mPresenter){
            mPresenter.attachView((V)this);
        }
    }
    /**
     * 创建presenter
     * @return
     */
    protected abstract T createPresenter();
    /**
     * 获取父布局Id
     * @return
     */
    protected abstract int getCreateViewLayoutId();
    /**
     * 绑定控件
     * 实现的时候第一个方法一定是setContentView();
     */
    public void findView(){}

    /**
     * 初始化布局
     */
    public void initView(){}
    @Override
    protected void onDestroy() {
        detachPresenter();
        AppManager.INSTANCE.removeTask(mWeakReference);
        LogUtils.d(getClass().getName(),"---------->销毁了");
        super.onDestroy();
    }
    private void initScreenData(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            mStatusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }
    private void detachPresenter(){
        //presenter与activity解绑定
        if(null != mPresenter){
            mPresenter.dispose();
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。参数为页面名称，可自定义)
        MobclickAgent.onPageStart(this.getClass().getName());
        //基本的统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。参数为页面名称，可自定义
        MobclickAgent.onPageEnd(this.getClass().getName());
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Override
    public void showUpdateDialog(boolean isForce, String url) {

    }

    @Override
    public void showError(String msg) {
        ToastUtils.showToast(msg);
    }
}
