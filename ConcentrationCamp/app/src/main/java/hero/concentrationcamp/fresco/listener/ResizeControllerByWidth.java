package hero.concentrationcamp.fresco.listener;

import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * 根据宽度和原图比例计算view的高度
 * Created by hero on 2016/12/13 0013.
 */

public class ResizeControllerByWidth extends BaseControllerListener<ImageInfo> {

    private final SimpleDraweeView draweeView;
    //view的宽度
    private final int mWidth;

    public ResizeControllerByWidth(SimpleDraweeView draweeView, int width) {
        this.draweeView = draweeView;
        this.mWidth=width;
    }

    @Override
    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
        if (imageInfo == null || draweeView == null) {
            return;
        }

        ViewGroup.LayoutParams vp = draweeView.getLayoutParams();
        int viewWidth = mWidth;
        int viewHeight = 0;
        int width = imageInfo.getWidth();
        int height = imageInfo.getHeight();
        float scale = viewWidth/(float)width;
        viewHeight = (int)(height*scale);
        vp.height = viewHeight;
        vp.width = viewWidth;
        draweeView.requestLayout();
    }

}

