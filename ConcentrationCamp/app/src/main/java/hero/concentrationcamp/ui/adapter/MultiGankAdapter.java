package hero.concentrationcamp.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import hero.concentrationcamp.GConfig;
import hero.concentrationcamp.R;
import hero.concentrationcamp.fresco.ImageLoader;
import hero.concentrationcamp.fresco.listener.ResizeControllerByWidth;
import hero.concentrationcamp.fresco.progressbar.CircleProgress;
import hero.concentrationcamp.mvp.model.entity.Gank;
import hero.concentrationcamp.mvp.presenter.GankSubFragmentPresenter;
import hero.concentrationcamp.ui.ImageActivity;
import hero.concentrationcamp.ui.WebActivity;
import hero.concentrationcamp.utils.PixelUtil;
import hero.concentrationcamp.utils.TimeUtils;
import hero.concentrationcamp.utils.UmengShare;

/**
 * Created by hero on 2016/12/16 0016.
 */

public class MultiGankAdapter extends BaseMultiItemQuickAdapter<Gank> {
    private int mScreenWidth;
    private GankSubFragmentPresenter mPresenter;
    private Gank detail;
    public MultiGankAdapter(int screenWidth,List<Gank> data,GankSubFragmentPresenter presenter){
        super(data);
        mScreenWidth = screenWidth;
        mPresenter = presenter;
        addItemType(Gank.IMAGE, R.layout.item_joke);
        addItemType(Gank.NORMAL, R.layout.item_gank);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Gank item, final int position) {
        String time = item.getPublishedAt();
        helper.setText(R.id.tv_time, TimeUtils.parseTzTime(time))
                .setText(R.id.tv_desc, item.getDesc());
        //收藏状态
        if(item.isCollected()){
            ((TextView)helper.getView(R.id.tv_collect)).setText("取消收藏");
        }else{
            ((TextView)helper.getView(R.id.tv_collect)).setText("收藏");
        }
        helper.getView(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("福利".equals(item.getType())){
                    UMImage image = new UMImage(mContext, item.getUrl());//网络图片
                    new UmengShare().openShareBoard((Activity) mContext,null
                            , item.getDesc()
                            , item.getUrl()
                            ,image);
                }else{
                    if(item.getImages()!=null
                            && item.getImages().size()>0){
                        UMImage image = new UMImage(mContext, item.getImages().get(0));//网络图片
                        new UmengShare().openShareBoard((Activity) mContext,null
                                , item.getDesc()
                                , item.getUrl()
                                ,image);
                    }else{
                        new UmengShare().openShareBoard((Activity) mContext,null
                                , item.getDesc()
                                , item.getUrl(),null);
                    }

                }
            }
        });
        helper.getView(R.id.btn_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.setCollectState(position,item);
            }
        });
        switch (helper.getItemViewType()) {
            case Gank.NORMAL:
                List<String> images = item.getImages();
                if(images!=null && images.size()>0){
                    if(!images.get(0).equals(helper.getView(R.id.iv_image).getTag())){
                        helper.getView(R.id.iv_image).setTag(images.get(0));
                        //有图就展示网络图
                        ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image)
                                ,images.get(0)+"?imageView2/0/w/300"
                                ,GConfig.IMAGE_SMALL, GConfig.IMAGE_SMALL);
                    }
                }else{
                    //展示占位图
                    ImageLoader.loadDrawable((SimpleDraweeView)helper.getView(R.id.iv_image),R.mipmap.ic_launcher);
                }
                helper.setText(R.id.tv_creator, item.getWho());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detail = item;
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("data", item);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case Gank.IMAGE:
                CircleProgress.Builder builder = new CircleProgress.Builder();
                builder.build().injectFresco((SimpleDraweeView)helper.getView(R.id.iv_image));
                if(!item.getUrl().equals(helper.getView(R.id.iv_image).getTag())){
                    //加tag 防止刷新闪烁
                    helper.getView(R.id.iv_image).setTag(item.getUrl());
                    //有图就展示网络图
                    ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image),item.getUrl()+"?imageView2/0/w/800"
                            ,new ResizeControllerByWidth((SimpleDraweeView)helper.getView(R.id.iv_image)
                                    ,mScreenWidth- PixelUtil.dp2px(40)));
                }

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
    public Gank getDetail(){
        return detail;
    }
}
