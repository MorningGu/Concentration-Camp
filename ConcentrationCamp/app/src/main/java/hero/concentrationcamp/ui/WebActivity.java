package hero.concentrationcamp.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.fresco.progressbar.CircleProgress;
import hero.concentrationcamp.mvp.contract.GankSubContract;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.presenter.GankSubFragmentPresenter;
import hero.concentrationcamp.rxjava.RxBusHelper;
import hero.concentrationcamp.ui.common.base.BaseActivity;
import hero.concentrationcamp.utils.PixelUtil;
import hero.concentrationcamp.utils.ToastUtils;
import hero.concentrationcamp.utils.UmengShare;

public class WebActivity extends BaseActivity<GankSubContract.IGankSubFragmentView,GankSubFragmentPresenter> implements GankSubContract.IGankSubFragmentView {
    WebView mWebView;
    Toolbar mToolbar;
    ImageView iv_loading;
    Gank data;
    CircleProgress loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent().getParcelableExtra("data");
        mWebView.loadUrl(data.getUrl());
        initToolBar(data.getDesc());
    }

    @Override
    protected GankSubFragmentPresenter createPresenter() {
        return new GankSubFragmentPresenter();
    }
    /**
     * 初始化toolbar
     */
    private void initToolBar(final String title){
        mToolbar.setTitle(title);//设置Toolbar标题
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_collect:
                        mPresenter.setCollectState(0,data);
                        break;
                    case R.id.action_share:
                        new UmengShare().openShareBoard(WebActivity.this,null,data.getDesc(),data.getUrl(),null);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateCollectMenuItem(menu);
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back键
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void findView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_loading = (ImageView)findViewById(R.id.iv_loading);
    }

    @Override
    public void initView() {
        initWebView();
        initLoading();
    }

    /**
     * 初始化laoding
     */
    private void initLoading(){
        loading = new CircleProgress.Builder()
                .setCircleRadius(PixelUtil.dp2px(30))
                .setCircleWidth(PixelUtil.dp2px(5))
                .build();
        loading.inject(iv_loading);
        loading.setTextColor(0xff252525);
        loading.setTextSize(PixelUtil.dp2px(16));
        loading.setMaxValue(100);
    }

    /**
     * 初始化webview
     */
    private void initWebView(){
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);    //支持javascript
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);     //缩放至屏幕的大小
        settings.setAllowFileAccess(true);      //设置可以访问文件
//        settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
        settings.setSupportZoom(true);    //设置支持缩放
        settings.setLoadsImagesAutomatically(true);    //设置自动加载图片
//        settings.setBlockNetworkImage(true);    //设置网页在加载的时候暂时不加载图片
//        settings.setAppCachePath("");   //设置缓存路径
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);   //设置缓存模式
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    iv_loading.setVisibility(View.GONE);
                } else if(newProgress == 0) {
                    iv_loading.setVisibility(View.VISIBLE);
                }
                loading.setLevel(newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                iv_loading.setVisibility(View.GONE);
                ToastUtils.showToast("加载失败");
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        mWebView.pauseTimers();
        mWebView.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        clearWebViewResource();
        super.onDestroy();
    }

    /**
     * 释放webview
     * Description: release the memory of web view, otherwise it's resource will not be recycle.
     * Created by Michael Lee on 7/18/16 20:38
     */
    public void clearWebViewResource() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
    }
    @Override
    protected int getCreateViewLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }
    private void updateCollectMenuItem(Menu menu){
        if(menu==null){
            return;
        }
        if(data.isCollected()){
            menu.getItem(0).setTitle("取消收藏");
        }else{
            menu.getItem(0).setTitle("收藏");
        }
    }
    @Override
    public void updateData(boolean isRefresh, List<Gank> data) {
        //不需要实现，这里用不到
    }

    /**
     * 收藏状态的改变
     * @param position
     */
    @Override
    public void updateItemState(int position) {
//        updateCollectMenuItem(mMenu);
        //发射数据
        RxBusHelper.post(data);
    }

    @Override
    public void updateData(Gank gank) {
        //无需实现
    }
}
