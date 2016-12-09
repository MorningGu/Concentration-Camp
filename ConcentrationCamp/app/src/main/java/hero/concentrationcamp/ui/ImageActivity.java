package hero.concentrationcamp.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

import hero.concentrationcamp.R;
import hero.concentrationcamp.mvp.BasePresenter;
import hero.concentrationcamp.utils.PixelUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class ImageActivity extends BaseActivity {
    private CompositeDisposable mDisposables;
    private static final String HTML_BEGIN = "<html><body bgcolor='#000000'>";
    private static final String HTML_MIDDLE = "<div style='width:100%;height:100%;display:table;'><span style='display:table-cell;vertical-align: middle;text-align:center;'><img src='file://";
    private static final String HTML_META_BEGIN = "<head><meta name='viewport' ";
    private static final String HTML_META_END = "'/></head>";
    private static final String HTML_END = "'></span></div></body></html>";
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        loadImage(url);
    }

    @Override
    public void findView() {
        //这里是为了使用application的context而不用activity的，因为可能会有泄露
        RelativeLayout layout_web = (RelativeLayout)findViewById(R.id.layout_web);
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout_web.addView(mWebView);
//        mWebView = (WebView)findViewById(R.id.webView);
    }

    @Override
    public void initView() {
        initWebView();
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
    }

    /**
     * 加载图片
     * @param url
     */
    private void loadImage(final String url){
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                //下载图片
                FutureTarget<File> futureTarget = Glide.with(getApplicationContext())
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL);
                String htmlURL = null;
                try {
                    File cache = futureTarget.get();
                    String path = cache.getAbsolutePath();
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
                    htmlURL = getHtmlChars(path, (int)(PixelUtil.px2dp(width)-(10*scale)), (int)(PixelUtil.px2dp(height)-(10*scale)));
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                } catch (ExecutionException exception) {
                    exception.printStackTrace();
                }
                e.onNext(htmlURL);
                e.onComplete();
            }
        }, BackpressureStrategy.DROP);
        ResourceSubscriber subscriber = new ResourceSubscriber<String>(){
            @Override
            public void onNext(String s) {
                if(!TextUtils.isEmpty(s)){
                    mWebView.loadDataWithBaseURL("", s, "text/html", null, null);
                }
            }

            @Override
            public void onError(Throwable t) {
                dispose();
            }

            @Override
            public void onComplete() {
                dispose();
            }
        };
        addSubscription(flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber));
    }

    /**
     * 这里的viewport是默认的
     * @param url
     * @param width
     * @param height
     * @return
     */
    private String getHtmlCharsByDefault(String url, int width,int height) {
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
                + 1.0 + HTML_META_END + HTML_MIDDLE + url + "' width='"+width+"' height='" + height + HTML_END;
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
        dispose();
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
    //取消所有的订阅
    public void dispose(){
        if(mDisposables!=null){
            mDisposables.clear();
        }
    }
    //rxjava 订阅
    protected void addSubscription(Disposable disposable) {
        if (disposable == null) return;
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(disposable);
    }
}
