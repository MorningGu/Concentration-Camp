package hero.concentrationcamp.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.mvp.IBaseView;
import hero.concentrationcamp.utils.ToastUtils;

/**
 * Created by hero on 2016/12/1 0001.
 */

public abstract class BaseFragment<V,T extends BasePresenter<V>> extends Fragment implements IBaseView {
    protected T mPresenter;
    /**
     * Fragment Content view
     */
    private View inflateView;
    /**
     * 记录是否已经创建了,防止重复创建
     */
    private boolean viewCreated;

    @Override
    final public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止重复调用onCreate方法，造成在initData方法中adapter重复初始化问题
        if (!viewCreated) {
            viewCreated = true;
            initData();
        }
    }
    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == inflateView) {
            int layoutResId = getCreateViewLayoutId();
            if (layoutResId > 0)
                inflateView = inflater.inflate(getCreateViewLayoutId(), container, false);

            // 解决点击穿透问题
            inflateView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        return inflateView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (viewCreated) {
            findView(view);
            initView(savedInstanceState);
            viewCreated = false;
        }
    }
    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 解决ViewPager中的问题
        if (null != inflateView) {
            ((ViewGroup) inflateView.getParent()).removeView(inflateView);
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
    public void onDestroy() {
        detachPresenter();
        super.onDestroy();
    }

    @Override
    public void initPresenter() {
        //初始化Presenter
        mPresenter = createPresenter();
        //presenter与View绑定
        if(null != mPresenter){
            mPresenter.attachView((V)this);
        }
    }

    /**
     * 初始化数据
     */
    @CallSuper
    public void initData(){
        initPresenter();
    }
    /**
     * 绑定控件
     * 实现的时候第一个方法一定是setContentView();
     */
    public void findView(View view){}

    /**
     * 初始化布局
     */
    public void initView(Bundle savedInstanceState){}
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
    @Override
    public void showUpdateDialog(boolean isForce, String url) {

    }

    @Override
    public void showError(String msg) {
        ToastUtils.showToast(msg);
    }
}
