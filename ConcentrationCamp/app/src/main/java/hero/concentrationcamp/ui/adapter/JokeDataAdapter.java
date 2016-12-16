package hero.concentrationcamp.ui.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.fresco.ImageLoader;
import hero.concentrationcamp.fresco.listener.ResizeControllerByWidth;
import hero.concentrationcamp.fresco.progressbar.CircleProgress;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.utils.PixelUtil;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeDataAdapter extends BaseQuickAdapter<Joke> {
    //屏幕宽度
    private int mScreenWidth;

    public JokeDataAdapter(List<Joke> data,int screenWidth) {
        super(R.layout.item_joke,data);
        mScreenWidth = screenWidth;
    }

    @Override
    protected void convert(final BaseViewHolder helper, Joke item) {
        helper.setText(R.id.tv_time, item.getCt())
              .addOnClickListener(R.id.btn_collect)
              .addOnClickListener(R.id.btn_share);
        if(item.getType()==1){
            //文字的时候显示text
            helper.setText(R.id.tv_desc, Html.fromHtml(item.getText()));
        }else{
            //图片的时候显示title
            helper.setText(R.id.tv_desc,item.getTitle());
        }
        //收藏状态
        if(item.isCollected()){
            ((Button)helper.getView(R.id.btn_collect)).setText("取消收藏");
        }else{
            ((Button)helper.getView(R.id.btn_collect)).setText("收藏");
        }
        if(!TextUtils.isEmpty(item.getImg())){
            helper.getView(R.id.iv_image).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.iv_image);
            CircleProgress.Builder builder = new CircleProgress.Builder();
            builder.build().injectFresco((SimpleDraweeView)helper.getView(R.id.iv_image));
            ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image),item.getImg()
                    ,new ResizeControllerByWidth((SimpleDraweeView)helper.getView(R.id.iv_image)
                            ,mScreenWidth- PixelUtil.dp2px(40)));
        }else{
            helper.getView(R.id.iv_image).setVisibility(View.GONE);
        }

    }
}
