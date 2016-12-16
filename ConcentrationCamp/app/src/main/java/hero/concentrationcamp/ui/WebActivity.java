package hero.concentrationcamp.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.ui.base.BaseActivity;
import hero.concentrationcamp.utils.ToastUtils;
import hero.concentrationcamp.utils.UmengShare;

public class WebActivity extends BaseActivity {
    WebView mWebView;
    Toolbar mToolbar;
    String url;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        mWebView.loadUrl(url);
        initToolBar(title);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
    /**
     * 初始化toolbar
     */
    private void initToolBar(String title){
        mToolbar.setTitle(title);//设置Toolbar标题
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_collect:
                ToastUtils.showToast("收藏了");
                break;
            case R.id.action_share:
                new UmengShare().openShareBoard(this,null,title,url,null);
                break;
        }
        return true;
    }
    @Override
    public void findView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void initView() {
        initWebView();
    }
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
//                if (newProgress == 100) {
//                    loading.setVisibility(View.GONE);
//                } else if(newProgress == 0) {
//                    loading.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if(StringUtil.isNotEmpty(title)){
//                    if(getHeaderLayout() != null){
//                        getHeaderLayout().setDefaultTitle(title);
//                    }
//                }
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
                ToastUtils.showToast("加载失败");
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        mWebView.pauseTimers();
        if (isFinishing()) {
            mWebView.loadUrl("about:blank");
        }
        mWebView.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
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
}
