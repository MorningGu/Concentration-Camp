package hero.concentrationcamp.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import hero.concentrationcamp.GConfig;
import hero.concentrationcamp.R;
import hero.concentrationcamp.fresco.ImageLoader;
import hero.concentrationcamp.fresco.listener.ResizeControllerByWidth;
import hero.concentrationcamp.fresco.progressbar.CircleProgress;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.ui.ImageActivity;
import hero.concentrationcamp.ui.WebActivity;
import hero.concentrationcamp.utils.PixelUtil;
import hero.concentrationcamp.utils.TimeUtils;

/**
 * Created by hero on 2016/12/16 0016.
 */

public class MultiGankAdapter extends BaseMultiItemQuickAdapter<Gank> {
    private int mScreenWidth;
    public MultiGankAdapter(int screenWidth,List<Gank> data){
        super(data);
        mScreenWidth = screenWidth;
        addItemType(Gank.IMAGE, R.layout.item_joke);
        addItemType(Gank.NORMAL, R.layout.item_gank);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Gank item) {
        String time = item.getPublishedAt();
        helper.setText(R.id.tv_time, TimeUtils.parseTzTime(time))
                .setText(R.id.tv_desc, item.getDesc())
                .addOnClickListener(R.id.btn_share)
                .addOnClickListener(R.id.btn_collect);
        //收藏状态
        if(item.isCollected()){
            ((Button)helper.getView(R.id.btn_collect)).setText("取消收藏");
        }else{
            ((Button)helper.getView(R.id.btn_collect)).setText("收藏");
        }
        switch (helper.getItemViewType()) {
            case Gank.NORMAL:
                List<String> images = item.getImages();
                if(images!=null && images.size()>0){
                    //有图就展示网络图
                    ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image),images.get(0)+"?imageView2/0/w/300", GConfig.IMAGE_SMALL, GConfig.IMAGE_SMALL);
                }else{
                    //展示占位图
                    ImageLoader.loadDrawable((SimpleDraweeView)helper.getView(R.id.iv_image),R.mipmap.ic_launcher);
                }
                helper.setText(R.id.tv_creator, item.getWho());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", item.getUrl());
                        intent.putExtra("text", item.getDesc());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case Gank.IMAGE:
                CircleProgress.Builder builder = new CircleProgress.Builder();
                builder.build().injectFresco((SimpleDraweeView)helper.getView(R.id.iv_image));
                ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image),item.getUrl()+"?imageView2/0/w/800"
                        ,new ResizeControllerByWidth((SimpleDraweeView)helper.getView(R.id.iv_image)
                                ,mScreenWidth- PixelUtil.dp2px(40)));
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ImageActivity.class);
                        intent.putExtra("url",item.getUrl());
                        intent.putExtra("text",item.getDesc());
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
    }
}
