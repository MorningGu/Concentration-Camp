package hero.concentrationcamp.utils;

import android.app.Activity;

import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;


/**
 * Created by hero on 2016/12/13 0013.
 */

public class UmengShare {

    /**
     * 分享面板的配置
     * @return
     */
    private ShareBoardConfig getShareBoardConfig(){
        ShareBoardConfig shareBoardConfig;
        shareBoardConfig = new ShareBoardConfig();
        shareBoardConfig.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);
        shareBoardConfig.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
        shareBoardConfig.setCancelButtonVisibility(false);
        shareBoardConfig.setTitleVisibility(false);
        return shareBoardConfig;
    }

    /**
     * 分享的监听回调
     * @return
     */
    private UMShareListener getUMShareListener(){
        return new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtils.showToast("分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtils.showToast("分享失败");
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showToast("取消分享");
            }
        };
    }

    /**
     * 打开分享面板分享图片
     * @param context
     * @param title
     * @param text
     * @param image
     */
    public void openShareBoard(Activity context, String title, String text, UMImage image){
        //如果应用未安装，跳转到appstore
        Config.isJumptoAppStore = true;
        new ShareAction(context)
                .withText(text)
                .withMedia(image)
                .withTitle("来自集中营的分享")
                .setCallback(getUMShareListener())
                .open(getShareBoardConfig());
    }
    /**
     * 打开分享面板分享文本
     * @param context
     * @param title
     * @param text
     */
    public void openShareBoard(Activity context, String title, String text){
        //如果应用未安装，跳转到appstore
        Config.isJumptoAppStore = true;
        new ShareAction(context)
                .withText(text)
                .withTitle("来自集中营的分享")
                .setCallback(getUMShareListener())
                .open(getShareBoardConfig());
    }
    /**
     * 打开分享面板分享链接
     * @param context
     * @param title
     * @param text
     * @param url
     */
    public void openShareBoard(Activity context, String title, String text,String url,UMImage image){
        //如果应用未安装，跳转到appstore
        Config.isJumptoAppStore = true;
        new ShareAction(context)
                .withText(text)
                .withTargetUrl(url)
                .withMedia(image)
                .withTitle("来自集中营的分享")
                .setCallback(getUMShareListener())
                .open(getShareBoardConfig());
    }
}
