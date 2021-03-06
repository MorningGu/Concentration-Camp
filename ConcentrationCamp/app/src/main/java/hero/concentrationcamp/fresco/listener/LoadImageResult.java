package hero.concentrationcamp.fresco.listener;

import android.graphics.Bitmap;

/**
 * 加载图片的结果监听器
 *
 * Created by android_ls on 16/9/10.
 */
public interface LoadImageResult {

    void onResult(Bitmap bitmap);
    void onResult(String path);
    void onProgress(float progress);

}
