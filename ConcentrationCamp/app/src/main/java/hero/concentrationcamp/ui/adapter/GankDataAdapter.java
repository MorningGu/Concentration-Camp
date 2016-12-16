package hero.concentrationcamp.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import hero.concentrationcamp.GConfig;
import hero.concentrationcamp.R;
import hero.concentrationcamp.fresco.ImageLoader;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.ui.ImageActivity;
import hero.concentrationcamp.ui.WebActivity;
import hero.concentrationcamp.utils.TimeUtils;

/**
 * Created by hero on 2016/12/6 0006.
 */

public class GankDataAdapter extends BaseQuickAdapter<Gank> {
    public GankDataAdapter(List data) {
        super(R.layout.item_gank,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Gank item) {
        String time = item.getPublishedAt();
        helper.setText(R.id.tv_time, TimeUtils.parseTzTime(time))
                .setText(R.id.tv_creator, item.getWho())
                .setText(R.id.tv_desc, item.getDesc())
                .addOnClickListener(R.id.btn_share)
                .addOnClickListener(R.id.btn_collect);
        //收藏状态
        if(item.isCollected()){
            ((Button)helper.getView(R.id.btn_collect)).setText("取消收藏");
        }else{
            ((Button)helper.getView(R.id.btn_collect)).setText("收藏");
        }
        if("福利".equals(item.getType())){
            //福利的图片是在url中的
            ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image),item.getUrl()+"?imageView2/0/w/300", GConfig.IMAGE_SMALL, GConfig.IMAGE_SMALL);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImageActivity.class);
                    intent.putExtra("url",item.getUrl());
                    intent.putExtra("text",item.getDesc());
                    mContext.startActivity(intent);
                }
            });
        }else{
            List<String> images = item.getImages();
            if(images!=null && images.size()>0){
                //有图就展示网络图
                ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image),images.get(0)+"?imageView2/0/w/300", GConfig.IMAGE_SMALL, GConfig.IMAGE_SMALL);
            }else{
                //展示占位图
                ImageLoader.loadDrawable((SimpleDraweeView)helper.getView(R.id.iv_image),R.mipmap.ic_launcher);
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("url", item.getUrl());
                    intent.putExtra("text", item.getDesc());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
