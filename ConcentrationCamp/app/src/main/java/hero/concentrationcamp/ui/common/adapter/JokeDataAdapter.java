package hero.concentrationcamp.ui.common.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import hero.concentrationcamp.R;
import hero.concentrationcamp.fresco.ImageLoader;
import hero.concentrationcamp.fresco.listener.ResizeControllerByWidth;
import hero.concentrationcamp.fresco.progressbar.CircleProgress;
import hero.concentrationcamp.mvp.model.entity.Joke;
import hero.concentrationcamp.mvp.presenter.JokeSubFragmentPresenter;
import hero.concentrationcamp.ui.ImageActivity;
import hero.concentrationcamp.utils.PixelUtil;
import hero.concentrationcamp.utils.UmengShare;

/**
 * Created by hero on 2016/12/8 0008.
 */

public class JokeDataAdapter extends BaseQuickAdapter<Joke> {
    //屏幕宽度
    private int mScreenWidth;
    private JokeSubFragmentPresenter mPresenter;
    public JokeDataAdapter(List<Joke> data, int screenWidth, JokeSubFragmentPresenter presenter) {
        super(R.layout.item_joke,data);
        mScreenWidth = screenWidth;
        mPresenter = presenter;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Joke item, final int position) {
        helper.setText(R.id.tv_time, item.getCt());
        if(item.getType()==1){
            //文字的时候显示text
            helper.setText(R.id.tv_desc, Html.fromHtml(item.getText()));
        }else{
            //图片的时候显示title
            helper.setText(R.id.tv_desc,item.getTitle());
        }
        //收藏状态
        if(item.isCollected()){
            ((TextView)helper.getView(R.id.tv_collect)).setText("取消收藏");
        }else{
            ((TextView)helper.getView(R.id.tv_collect)).setText("收藏");
        }
        helper.getView(R.id.iv_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra("url",item.getImg());
                if(item.getType()==1){
                    //文字的时候显示text
                    intent.putExtra("text",item.getText());
                }else{
                    //图片的时候显示title
                    intent.putExtra("text",item.getTitle());
                }
                mContext.startActivity(intent);
            }
        });
        helper.getView(R.id.btn_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.setCollectState(position,item);
            }
        });
        helper.getView(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text;
                if(item.getType()==1){
                    //文字的时候显示text
                    text = item.getText();
                }else{
                    //图片的时候显示title
                    text = item.getTitle();
                }
                if(TextUtils.isEmpty(item.getImg())){
                    new UmengShare().openShareBoard((Activity) mContext,text,text);
                }else{
                    UMImage image = new UMImage(mContext, item.getImg());//网络图片
                    new UmengShare().openShareBoard((Activity) mContext,text,text,item.getImg(),image);
                }
            }
        });
        if(!TextUtils.isEmpty(item.getImg())){
            //加tag 防止刷新闪烁
            if(!item.getImg().equals(helper.getView(R.id.iv_image).getTag())){
                helper.getView(R.id.iv_image).setTag(item.getImg());
                helper.getView(R.id.iv_image).setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.iv_image);
                CircleProgress.Builder builder = new CircleProgress.Builder();
                builder.build().injectFresco((SimpleDraweeView)helper.getView(R.id.iv_image));
                ImageLoader.loadImage((SimpleDraweeView)helper.getView(R.id.iv_image),item.getImg()
                        ,new ResizeControllerByWidth((SimpleDraweeView)helper.getView(R.id.iv_image)
                                ,mScreenWidth- PixelUtil.dp2px(40)));
            }
        }else{
            helper.getView(R.id.iv_image).setVisibility(View.GONE);
        }
    }
}
