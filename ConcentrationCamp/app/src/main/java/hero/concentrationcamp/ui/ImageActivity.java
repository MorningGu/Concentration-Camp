package hero.concentrationcamp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;

import hero.concentrationcamp.R;
import hero.concentrationcamp.fresco.ImageLoader;
import hero.concentrationcamp.fresco.listener.LoadImageResult;
import hero.concentrationcamp.fresco.progressbar.CircleProgress;
import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.ui.base.BaseActivity;
import hero.concentrationcamp.utils.PixelUtil;
import hero.concentrationcamp.utils.UmengShare;

public class ImageActivity extends BaseActivity {
    private static final String HTML_BEGIN = "<html><body bgcolor='#000000'>";
    private static final String HTML_MIDDLE = "<div style='width:100%;height:100%;display:table;'><span style='display:table-cell;vertical-align: middle;text-align:center;'><img src='file:";
    private static final String HTML_META_BEGIN = "<head><meta name='viewport' ";
    private static final String HTML_META_END = "'/></head>";
    private static final String HTML_END = "'></span></div></body></html>";
    String url;
    String text;
    WebView mWebView;
    Button btn_share;
    ImageView iv_loading;
    CircleProgress loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        text = getIntent().getStringExtra("text");
        mWebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWebView.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                //WebView加载完毕后才开始加载图片
                loadImage(url);
            }
        });
    }

    @Override
    public void findView() {
        btn_share = (Button)findViewById(R.id.btn_share);
        //这里是为了使用application的context而不用activity的，因为可能会有泄露
        RelativeLayout layout_web = (RelativeLayout)findViewById(R.id.layout_web);
        iv_loading = (ImageView)findViewById(R.id.iv_loading);
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout_web.addView(mWebView);
    }

    @Override
    public void initView() {
        initWebView();
        loading = new CircleProgress.Builder().build();
        loading.initProperty();
        loading.inject(iv_loading);
        loading.setMaxValue(100);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMImage image = new UMImage(ImageActivity.this, url);//网络图片
                new UmengShare().openShareBoard(ImageActivity.this,null,text,image);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 初始化webview
     */
    private void initWebView(){
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(false);
        //能手势缩放但不显示缩放按钮
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mWebView.setBackgroundColor(0x00000000);//xml中设置背景无效，去掉有白底
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    iv_loading.setVisibility(View.GONE);
                }
            }


        });
    }

    /**
     * 加载图片
     * @param url
     */
    private void loadImage(final String url){
        ImageLoader.loadImageToReturnPath(this, url, new LoadImageResult() {
            @Override
            public void onResult(Bitmap bitmap) {

            }

            @Override
            public void onProgress(final float progress) {
                //进度显示
                if(iv_loading.getVisibility()==View.GONE){
                    iv_loading.setVisibility(View.VISIBLE);
                }
                loading.setLevel((int)(progress*100));
            }

            @Override
            public void onResult(String path) {
                if(isFinishing() || mWebView==null){
                    return;
                }
                //获得图片宽高
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                float scale = 1;
                float width = options.outWidth;
                float height = options.outHeight;
                int viewWidth = mWebView.getWidth();
                int viewHeight = mWebView.getHeight();
                if(options.outWidth>options.outHeight) {//如果图片是横图
                    //超过屏幕宽度并小于屏幕高度，则原比例居中显示
                    if(options.outWidth>viewWidth && options.outHeight<=viewHeight){
                        scale = 1;
                    }
                    //超过屏幕宽度并大于屏幕高度 将高度缩放到屏幕高度的大小，宽按同样比例缩小
                    if(options.outWidth>viewWidth && options.outHeight>viewHeight){
                        scale = (float) viewHeight/options.outHeight;
                        height = viewHeight;
                        width = (width*scale);
                    }
                    //不超过屏幕宽度，将宽度放大至屏幕宽度的大小，高按同样比例放大
                    if(options.outWidth<viewWidth){
                        scale = (float) viewWidth/options.outWidth;
                        width = viewWidth;
                        height = (height*scale);
                    }
                } else {
                    //如果图片是竖图
                    scale = (float) viewWidth/width;
                    width = viewWidth;
                    height = (height*scale);
                }
                //最后按比例减去10（自己估计的值），是因为如果不减掉，会出现加载的图略大一些的情况
//                    htmlURL = getHtmlCharsByDefault(path, (int)(PixelUtil.px2dp(width)-(10*scale)), (int)(PixelUtil.px2dp(height)-(10*scale)));
                String htmlURL = getHtmlChars(path, (int)(PixelUtil.px2dp(width)-(10*scale)), (int)(PixelUtil.px2dp(height)-(10*scale)));
                mWebView.loadDataWithBaseURL("", htmlURL, "text/html", null, null);
            }
        });
        iv_loading.setVisibility(View.VISIBLE);
    }

    /**
     * 这里的viewport是默认的
     * @param url
     * @param width
     * @param height
     * @return
     */
    private String getHtmlCharsDefault(String url, int width,int height) {
        return HTML_BEGIN + HTML_MIDDLE + url + "' width='" + width +"' height='"+height+"'"+ HTML_END;
    }

    /**
     * 这里的viewport是1
     * @param url
     * @param width
     * @param height
     * @return
     */
    private String getHtmlChars(String url, int width, int height) {
        return HTML_BEGIN + HTML_META_BEGIN + "content='minimum-scale="
                + 1.0 + HTML_META_END + HTML_MIDDLE + url + "' width='"+width+ HTML_END;
    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getCreateViewLayoutId() {
        return R.layout.activity_image;
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
}
