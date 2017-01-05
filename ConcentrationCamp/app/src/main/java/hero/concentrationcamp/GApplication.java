package hero.concentrationcamp;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import hero.concentrationcamp.fresco.config.ImageLoaderConfig;
import hero.concentrationcamp.mvp.model.greendao.GreenDaoHelper;
import hero.concentrationcamp.utils.LogUtils;


/**
 * Created by hero on 2016/11/30.
 */
public class GApplication extends Application {

    private static GApplication sInstance; //s的前缀，表示static m的前缀表示member

    private boolean isDebug = false;

    public void onCreate() {
        super.onCreate();
        init();
    }
    private void init(){
        sInstance = this;
        initUMShare();
        initLeakCanary();
        initDebug();
        initFresco();
        GreenDaoHelper.initDatabase();
    }
    /**
     * 友盟分享 初始化
     */
    private void initUMShare(){
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxac488e4c6ab1640e", "5bc3b0a8121dd0a18df8d5ad14739658");
        PlatformConfig.setSinaWeibo("1325446430", "fd5a4f00412f9e66ddc4f25f68f11867");
        PlatformConfig.setQQZone("1104842168", "Is67JxDKFPCzUKgw");
    }

    private void initLeakCanary(){
        //内存分析工具的初始化 针对1.5版本
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
    private void initFresco(){
        Fresco.initialize(this, ImageLoaderConfig.getImagePipelineConfig(this));
//        Fresco.initialize(this);
    }
    /**
     * 初始化是否是debug
     */
    private void initDebug(){
        ApplicationInfo appInfo = null;
        try {
            appInfo = GApplication.getInstance().getPackageManager()
                    .getApplicationInfo(GApplication.getInstance().getPackageName(),
                            PackageManager.GET_META_DATA);
            isDebug =  appInfo.metaData.getBoolean("IS_DEBUG");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.isDebug(isDebug);
    }

    /**
     * 是不是debug
     * @return
     */
    public boolean isDebug(){
        return isDebug;
    }

    /**
     * 得到Application实例
     * @return
     */
    public static GApplication getInstance() {
        return sInstance;
    }

}
